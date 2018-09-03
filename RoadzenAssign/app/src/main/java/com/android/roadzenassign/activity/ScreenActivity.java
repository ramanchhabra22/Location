package com.android.roadzenassign.activity;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.roadzenassign.R;
import com.android.roadzenassign.adapter.ScreenDataAdapter;
import com.android.roadzenassign.constants.ParamConstant;
import com.android.roadzenassign.constants.TypeConstant;
import com.android.roadzenassign.model.QuestionModel;
import com.android.roadzenassign.model.ScreenModel;
import com.android.roadzenassign.viewModel.ScreenViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import static com.android.roadzenassign.constants.AppConstant.CAMERA_PERMISSION_CODE;
import static com.android.roadzenassign.constants.AppConstant.CAMERA_REQUEST;
import static com.android.roadzenassign.constants.AppConstant.LOCATION_PERMISSION_CODE;
import static com.android.roadzenassign.constants.AppConstant.SCREEN_1;
import static com.android.roadzenassign.constants.AppConstant.SCREEN_2;

public class ScreenActivity extends BaseActivity {


  private ScreenViewModel mModel;
  private RecyclerView recyclerView;
  private ImageView imageView;
  private Button button;
  private Button locationButton;

  private ArrayList<QuestionModel> questionModels;
  private boolean imageUploaded = false;
  private FusedLocationProviderClient mFusedLocationClient;
  private String addressLine;
  private String postalCode;
  private EditText customerAddressView;
  private EditText zipCodeView;


  @Override
  public int getContentLayout() {
    return R.layout.activity_screen;
  }

  @Override
  public void initComponents() {
    recyclerView = findViewById(R.id.as_recyclerView);
    button = findViewById(R.id.as_button);
    locationButton = findViewById(R.id.as_location_track);
  }

  @Override
  protected void onStart() {
    super.onStart();
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

  }

  @Override
  public void initListner() {
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (checkValidation()) {
          mModel.setScreenModelData(null);
          if (button.getText().toString().equals(getResources().getString(R.string.screen_1))) {
            screenDataObserver(SCREEN_1);
            button.setText(getResources().getString(R.string.screen_2));
          } else {
            screenDataObserver(SCREEN_2);
            button.setText(getResources().getString(R.string.screen_1));
          }
        }
      }

    });

    locationButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(ScreenActivity.this, LocationActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public void initModel() {
    mModel = ViewModelProviders.of(this).get(ScreenViewModel.class);
  }

  @Override
  public void callObserver() {
    screenDataObserver(SCREEN_1);
    button.setText(getResources().getString(R.string.screen_2));
  }

  @Override
  protected void onResume() {
    super.onResume();
    checkLocationPermission();
  }

  private void screenDataObserver(String jsonFileName) {
    final Observer<ScreenModel> screenModelObserver = new Observer<ScreenModel>() {
      @Override
      public void onChanged(@Nullable final ScreenModel screenModel) {
        if (screenModel != null) {
          setTitle(screenModel.header);
          setData(screenModel.questionList);
        }
      }
    };
    mModel.getScreenModel(jsonFileName).observe(this, screenModelObserver);
  }

  private void setData(ArrayList<QuestionModel> questionModels) {
    this.questionModels = questionModels;
    ScreenDataAdapter screenDataAdapter = new ScreenDataAdapter(this, this.questionModels);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(screenDataAdapter);
  }

  public void captureImage(ImageView imageView) {
    this.imageView = imageView;
    checkCameraPermission();
  }

  private void checkCameraPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
      } else {
        startCamera();
      }
    }
  }

  private void checkLocationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
      } else {
        getLastLocation();
      }
    }
  }


  private void getLastLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    mFusedLocationClient.getLastLocation()
        .addOnCompleteListener(this, new OnCompleteListener<Location>() {
          @Override
          public void onComplete(@NonNull Task<Location> task) {
            if (task.isSuccessful() && task.getResult() != null) {
              Location location = task.getResult();
              getAddress(location);
            }
          }
        });
  }


  private void getAddress(Location location) {
    if (location != null) {
      double longitude = location.getLongitude();
      double latitude = location.getLatitude();
      Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
      try {
        List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (listAddresses != null && listAddresses.size() > 0) {
          setAddress(listAddresses);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void setAddress(List<Address> listAddresses) {
    addressLine = listAddresses.get(0).getAddressLine(0);
    Address address = listAddresses.get(0);
    postalCode = address.getPostalCode();

    if (customerAddressView != null) {
      customerAddressView.setText(addressLine);
    }

    if (postalCode != null) {
      zipCodeView.setText(postalCode);
    }
  }


  private void startCamera() {
    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(cameraIntent, CAMERA_REQUEST);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == CAMERA_PERMISSION_CODE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        startCamera();
      } else {
        Toast.makeText(this, getResources().getString(R.string.camera_permission_denied), Toast.LENGTH_LONG).show();
      }
    } else if (requestCode == LOCATION_PERMISSION_CODE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        getLastLocation();
      } else {
        Toast.makeText(this, getResources().getString(R.string.location_permission_denied), Toast.LENGTH_LONG).show();
      }
    }
  }


  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == CAMERA_REQUEST) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
          Bitmap photo = (Bitmap) bundle.get(ParamConstant.DATA);
          imageView.setImageBitmap(photo);
          imageUploaded = true;
        }

      }
    }
  }

  public void setCustomerAddressView(EditText customerAddressView) {
    this.customerAddressView = customerAddressView;
    if (addressLine == null) {
      getLastLocation();
    } else {
      customerAddressView.setText(addressLine);
    }
  }


  public void setZipCodeView(EditText zipCodeView) {
    this.zipCodeView = zipCodeView;
    if (postalCode == null) {
      getLastLocation();
    } else {
      zipCodeView.setText(postalCode);
    }
  }

  private boolean checkValidation() {
    boolean flag = false;
    for (QuestionModel questionModel : questionModels) {
      String type = questionModel.type;
      if (type.equals(TypeConstant.TYPE_IMAGE)) {
        if (!imageUploaded) {
          Toast.makeText(this, getResources().getString(R.string.upload_image), Toast.LENGTH_SHORT).show();
          return imageUploaded;
        }

      } else if (type.equals(TypeConstant.TYPE_TEXT) || type.equals(TypeConstant.TYPE_TEXT_NUMERIC)) {
        String value = questionModel.value;
        if (value == null || value.isEmpty()) {
          Toast.makeText(this, String.format(getResources().getString(R.string.enter_correct), questionModel.hint), Toast.LENGTH_SHORT).show();
          return false;
        } else if (questionModel.validationModel != null) {
          if (type.equals(TypeConstant.TYPE_TEXT)) {
            String check = questionModel.validationModel.check;
            if (check.equals(TypeConstant.CHECK_EMAL)) {
              if (!isValidEmail(value)) {
                Toast.makeText(this, String.format(getResources().getString(R.string.enter_correct), questionModel.hint) + questionModel.hint, Toast.LENGTH_SHORT).show();
                return false;
              } else {
                flag = true;
              }
            }
          } else {
            int size = questionModel.validationModel.size;
            int len = value.length();
            if (len != size) {
              Toast.makeText(this, String.format(getResources().getString(R.string.enter_correct), questionModel.hint) + questionModel.hint, Toast.LENGTH_SHORT).show();
              return false;
            }
            flag = true;
          }
        } else {
          flag = true;
        }
      }
    }
    return flag;
  }

  public boolean isValidEmail(String email) {
    if (email != null) {
      Matcher emailMatcher = android.util.Patterns.EMAIL_ADDRESS.matcher(email);
      return emailMatcher.matches();
    } else {
      return false;
    }
  }
}
