package id.ac.astra.polman.nim0320220008.myled.gakepake;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import id.ac.astra.polman.nim0320220008.myled.R;

public class MainActivity3 extends AppCompatActivity {

    private TextView textViewColor;
    private SeekBar seekBarRed, seekBarGreen, seekBarBlue;
    private int red = 0, green = 0, blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nyoba_warna);

        textViewColor = findViewById(R.id.text_view_color);
        seekBarRed = findViewById(R.id.seek_bar_red);
        seekBarGreen = findViewById(R.id.seek_bar_green);
        seekBarBlue = findViewById(R.id.seek_bar_blue);

        seekBarRed.setOnSeekBarChangeListener(colorChangeListener);
        seekBarGreen.setOnSeekBarChangeListener(colorChangeListener);
        seekBarBlue.setOnSeekBarChangeListener(colorChangeListener);
    }

    private SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == R.id.seek_bar_red) {
                red = progress;
            } else if (seekBar.getId() == R.id.seek_bar_green) {
                green = progress;
            } else if (seekBar.getId() == R.id.seek_bar_blue) {
                blue = progress;
            }
            updateColorView();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }
    };

    private void updateColorView() {
        int color = Color.rgb(red, green, blue);
        textViewColor.setBackgroundColor(color);
        textViewColor.setText(String.format("RGB: (%d, %d, %d)", red, green, blue));
    }
}
