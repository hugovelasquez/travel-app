package com.sucaldo.travelapp.views;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sucaldo.travelapp.R;
import com.sucaldo.travelapp.db.DatabaseHelper;
import com.sucaldo.travelapp.model.CityLocation;

import java.util.List;

public class WorldMapFragment extends Fragment {

    private Bitmap worldBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.world_map_view, container, false);

        DatabaseHelper myDB = new DatabaseHelper(getContext());
        int totalKms = myDB.getTotalTravelledKms();
        int WORLD_CIRCUMFERENCE = 40075;
        float timesAroundWorld = (float) totalKms / WORLD_CIRCUMFERENCE;

        TextView timesAroundWorldText = rootView.findViewById(R.id.world_map_travel);
        timesAroundWorldText.setText(getString(R.string.text_world_map_travel, timesAroundWorld));

        ImageView worldMap = rootView.findViewById(R.id.map);

        Canvas worldMapCanvas = getWorldMapAsCanvasAndSetBitmap();

        List<CityLocation> circleLocations = myDB.getLatitudeAndLongitudeOfVisitedCities();
        for (CityLocation circleLocation : circleLocations) {
            drawLocationCircleOnWorldCanvas(worldMapCanvas, circleLocation.getLatitude(), circleLocation.getLongitude());
        }

        worldMap.setImageDrawable(new BitmapDrawable(getResources(), worldBitmap));

        return rootView;
    }


    private Canvas getWorldMapAsCanvasAndSetBitmap() {
        Bitmap myBitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.world_map);
        worldBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(worldBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        return tempCanvas;
    }


    private void drawLocationCircleOnWorldCanvas(Canvas worldMap, float latitude, float longitude) {
        float latitudePosition = getLatitudePosition(latitude);
        float longitudePosition = getLongitudePosition(longitude);

        Paint myPaint = new Paint();
        myPaint.setColor(getContext().getColor(R.color.teal));
        worldMap.drawCircle(longitudePosition, latitudePosition, 10, myPaint);
    }


    private float getLatitudePosition(float latitude) {
        float CANVAS_HEIGHT = 842;
        float pixelsLatitude = latitude * CANVAS_HEIGHT / 180;
        // Calibration factors defined manually by trial and error
        float CALIBRATION_FACTOR_LAT2 = 9;
        if (latitude > 0 && latitude < 35) {
            pixelsLatitude = (CANVAS_HEIGHT / 2) - pixelsLatitude - CALIBRATION_FACTOR_LAT2;
        }
        // Calibration factors defined manually by trial and error
        float CALIBRATION_FACTOR_LAT1 = 30;
        if (latitude >= 35) {
            pixelsLatitude = (CANVAS_HEIGHT / 2) - pixelsLatitude - CALIBRATION_FACTOR_LAT1;
        }
        if (latitude < 0 && latitude > -35) {
            pixelsLatitude = (CANVAS_HEIGHT / 2) + (Math.abs(pixelsLatitude)) + CALIBRATION_FACTOR_LAT2;
        }
        if (latitude <= -35) {
            pixelsLatitude = (CANVAS_HEIGHT / 2) + (Math.abs(pixelsLatitude)) + CALIBRATION_FACTOR_LAT1;
        }
        if (latitude == 0) {
            pixelsLatitude = CANVAS_HEIGHT / 2;
        }
        return pixelsLatitude;
    }


    private float getLongitudePosition(float longitude) {
        // Width and Height optimized for a tablet with resolution 2048 x 1536 xhdpi
        float CANVAS_WIDTH = 1900;
        float pixelsLongitude = longitude * CANVAS_WIDTH / 360 + (CANVAS_WIDTH / 2);
        if (longitude > 0) {
            float CALIBRATION_FACTOR_LONG = 7;
            pixelsLongitude = pixelsLongitude - CALIBRATION_FACTOR_LONG;
        }
        return pixelsLongitude;
    }


}
