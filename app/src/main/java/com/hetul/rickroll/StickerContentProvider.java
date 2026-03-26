package com.hetul.rickroll;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;

public class StickerContentProvider extends ContentProvider {

    // --- Pack metadata ---
    private static final String PACK_ID        = "rickroll_cats_1";
    private static final String PACK_NAME      = "CAT - 1";
    private static final String PACK_AUTHOR    = "Hetul";
    // This URL is what "View sticker pack" button opens 😈
    private static final String PUBLISHER_URL  = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private static final String PRIVACY_URL    = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private static final String LICENSE_URL    = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private static final String TRAY_ICON      = "tray_icon.png";
    private static final boolean ANIMATED      = false;

    private static final String[][] STICKERS = {
        {"sticker_01.webp", "😸"},
        {"sticker_02.webp", "😹"},
        {"sticker_03.webp", "😺"},
        {"sticker_04.webp", "😻"},
        {"sticker_05.webp", "😼"},
        {"sticker_06.webp", "😽"},
        {"sticker_07.webp", "🙀"},
        {"sticker_08.webp", "😾"},
        {"sticker_09.webp", "😿"},
        {"sticker_10.webp", "🐱"},
    };

    // --- ContentProvider columns (WhatsApp spec) ---
    private static final String STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier";
    private static final String STICKER_PACK_NAME_IN_QUERY       = "sticker_pack_name";
    private static final String STICKER_PACK_PUBLISHER_IN_QUERY  = "sticker_pack_publisher";
    private static final String STICKER_PACK_ICON_IN_QUERY       = "sticker_pack_tray_image_file";
    private static final String ANDROID_APP_DOWNLOAD_LINK        = "android_play_store_link";
    private static final String IOS_APP_DOWNLOAD_LINK            = "ios_app_download_link";
    private static final String PUBLISHER_WEBSITE                = "sticker_pack_publisher_website";
    private static final String PRIVACY_POLICY_WEBSITE           = "sticker_pack_privacy_policy_website";
    private static final String LICENSE_AGREEMENT_WEBSITE        = "sticker_pack_license_agreement_website";
    private static final String ANIMATED_STICKER_PACK            = "animated_sticker_pack";

    private static final String STICKER_FILE_NAME_IN_QUERY       = "sticker_file_name";
    private static final String STICKER_FILE_EMOJI_IN_QUERY      = "sticker_emoji";

    private static final int METADATA = 1;
    private static final int STICKERS_URI = 2;
    private static final int STICKERS_ASSET = 3;

    private UriMatcher uriMatcher;
    private static final String AUTHORITY = "com.hetul.rickroll.stickercontentprovider";

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "metadata", METADATA);
        uriMatcher.addURI(AUTHORITY, "stickers/*", STICKERS_URI);
        uriMatcher.addURI(AUTHORITY, "stickers_asset/*/*", STICKERS_ASSET);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case METADATA:
                return getPackList();
            case STICKERS_URI:
                return getStickerList(uri.getLastPathSegment());
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private Cursor getPackList() {
        MatrixCursor cursor = new MatrixCursor(new String[]{
            STICKER_PACK_IDENTIFIER_IN_QUERY,
            STICKER_PACK_NAME_IN_QUERY,
            STICKER_PACK_PUBLISHER_IN_QUERY,
            STICKER_PACK_ICON_IN_QUERY,
            ANDROID_APP_DOWNLOAD_LINK,
            IOS_APP_DOWNLOAD_LINK,
            PUBLISHER_WEBSITE,
            PRIVACY_POLICY_WEBSITE,
            LICENSE_AGREEMENT_WEBSITE,
            ANIMATED_STICKER_PACK
        });
        cursor.addRow(new Object[]{
            PACK_ID,
            PACK_NAME,
            PACK_AUTHOR,
            TRAY_ICON,
            "",            // no Play Store link
            "",            // no iOS link
            PUBLISHER_URL, // <-- rickroll goes here
            PRIVACY_URL,
            LICENSE_URL,
            ANIMATED ? 1 : 0
        });
        return cursor;
    }

    private Cursor getStickerList(String packId) {
        if (!PACK_ID.equals(packId)) return null;
        MatrixCursor cursor = new MatrixCursor(new String[]{
            STICKER_FILE_NAME_IN_QUERY,
            STICKER_FILE_EMOJI_IN_QUERY
        });
        for (String[] sticker : STICKERS) {
            cursor.addRow(new Object[]{sticker[0], sticker[1]});
        }
        return cursor;
    }

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, String mode) {
        if (uriMatcher.match(uri) == STICKERS_ASSET) {
            // URI format: stickers_asset/{pack_id}/{filename}
            String[] segments = uri.getPathSegments().toArray(new String[0]);
            if (segments.length == 3) {
                String filename = segments[2];
                try {
                    return getContext().getAssets().openFd("contents/" + filename);
                } catch (IOException e) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override public String getType(Uri uri) { return null; }
    @Override public Uri insert(Uri uri, ContentValues values) { throw new UnsupportedOperationException(); }
    @Override public int delete(Uri uri, String sel, String[] args) { throw new UnsupportedOperationException(); }
    @Override public int update(Uri uri, ContentValues v, String sel, String[] args) { throw new UnsupportedOperationException(); }
}
