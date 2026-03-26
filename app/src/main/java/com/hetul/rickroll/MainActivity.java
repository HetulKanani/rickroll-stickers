package com.hetul.rickroll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int ADD_PACK_REQUEST = 200;
    private static final String PACK_ID = "rickroll_cats_1";
    private static final String PACK_NAME = "CAT - 1";
    private static final String PACK_AUTHOR = "Hetul";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = findViewById(R.id.tv_title);
        title.setText(PACK_NAME + " by " + PACK_AUTHOR);

        Button addBtn = findViewById(R.id.btn_add);
        addBtn.setOnClickListener(v -> addStickerPackToWhatsApp());
    }

    private void addStickerPackToWhatsApp() {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra("sticker_pack_id", PACK_ID);
        intent.putExtra("sticker_pack_authority", "com.hetul.rickroll.stickercontentprovider");
        intent.putExtra("sticker_pack_name", PACK_NAME);
        try {
            startActivityForResult(intent, ADD_PACK_REQUEST);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PACK_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                String error = data != null ? data.getStringExtra("validation_error") : "Unknown";
                Toast.makeText(this, "Failed: " + error, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Sticker pack added!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
