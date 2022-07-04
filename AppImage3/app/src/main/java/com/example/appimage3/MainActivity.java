package com.example.appimage3;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appimage3.Adapter.ViewPagerAdapter;
import com.example.appimage3.Interface.AddTextFragmentListener;
import com.example.appimage3.Interface.BrushFragmentListener;
import com.example.appimage3.Interface.EditImageFragmentListener;
import com.example.appimage3.Interface.EmojiFragmentListener;
import com.example.appimage3.Interface.FiltersListFragmentListener;
import com.example.appimage3.Utils.BitMapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class MainActivity extends AppCompatActivity implements FiltersListFragmentListener, EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener, AddTextFragmentListener {

    public static final String pictureName="abc.jpg";
    public static final int PERMISSION_PICK_IMAGE=1000;
    public static final int PERMISSION_INSERT_IMAGE=2000;
    public static final int CAMERA_REQUEST_CODE=3000;

    Button btn_openimg;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;

    CoordinatorLayout coordinatorLayout;

    Bitmap originalBitmap, filteredBitmap, finalBitmap;

    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;
    ImageButton btn_open_gallery,btn_translate,btn_camera;

    CardView btn_filter_list, btn_edit, btn_brush, btn_emoji, btn_add_text, btn_add_image, btn_crop;

    Uri uri_img_selected;

    int brightnessFinal=0;
    float saturationFinal=1.0f;
    float constrantFinal=1.0f;

    //load native image filters lib
    static{
        System.loadLibrary("NativeImageProcessor");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("App Processing Image");

        //view
        photoEditorView=(PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                                                    .setPinchTextScalable(true)
                                                    .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"NotoColorEmoji.ttf"))
                                                    .build();
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator);

        btn_filter_list=(CardView)findViewById(R.id.btn_filter_list);
        btn_edit=(CardView) findViewById(R.id.btn_edit);
        btn_brush=(CardView)findViewById(R.id.btn_brush);
        btn_emoji=(CardView)findViewById(R.id.btn_emoji);
        btn_add_text=(CardView)findViewById(R.id.btn_add_text);
        btn_add_image=(CardView)findViewById(R.id.btn_add_image);
        btn_open_gallery=(ImageButton)findViewById(R.id.btn_gallery_img);
        btn_translate=(ImageButton)findViewById(R.id.btn_translate);
        btn_camera=(ImageButton) findViewById(R.id.btn_camera);


        btn_crop=(CardView) findViewById(R.id.btn_crop_image);

        btn_open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFromGallery();
            }
        });

        btn_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });


        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCrop(uri_img_selected);
            }
        });

        btn_filter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filtersListFragment != null)
                {
                 filtersListFragment.show(getSupportFragmentManager(),filtersListFragment.getTag());
                }
                else
                {
                    FiltersListFragment filtersListFragment=FiltersListFragment.getInstance(null);
                    filtersListFragment.setListener(MainActivity.this);
                    filtersListFragment.show(getSupportFragmentManager(),filtersListFragment.getTag());

                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditImageFragment editImageFragment=EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });

        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set true to turn on mode brush
                photoEditor.setBrushDrawingMode(true);

                BrushFragment brushFragment=BrushFragment.getInstance();
                brushFragment.setListener(MainActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });

        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiFragment emojiFragment=EmojiFragment.getInstance();
                emojiFragment.setListener(MainActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());


            }
        });

        btn_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTextFragment addTextFragment=AddTextFragment.getInstance();
                addTextFragment.setListener(MainActivity.this);
                addTextFragment.show(getSupportFragmentManager(),addTextFragment.getTag());
            }
        });

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagetoPicture();
            }
        });

        loadImage();



    }

    private void startCrop(Uri uri_img_selected) {
        String destinationFilename= new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop uCrop=UCrop.of(uri_img_selected,Uri.fromFile(new File(getCacheDir(),destinationFilename)));
        uCrop.start(MainActivity.this);
    }

    private void addImagetoPicture() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted())
                        {
                            Intent intent=new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_INSERT_IMAGE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(MainActivity.this,"Error, Permission denied",Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    private void loadImage() {
        originalBitmap= BitMapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap =originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap= originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(originalBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());

        filtersListFragment =new FiltersListFragment();
        filtersListFragment.setListener(this);

        editImageFragment=new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment,"Filters");
        adapter.addFragment(editImageFragment,"EDIT");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal=brightness;
        Filter myfilter=new Filter();
        myfilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal=saturation;
        Filter myfilter=new Filter();
        myfilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onConstrantChanged(float constrant) {
        constrantFinal=constrant;
        Filter myfilter=new Filter();
        myfilter.addSubFilter(new ContrastSubFilter(constrant));
        photoEditorView.getSource().setImageBitmap(myfilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap=filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

        Filter myFilter=new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));

        finalBitmap= myFilter.processFilter(bitmap);
    }

    @Override
    public void onFilterSeleted(Filter filter) {
        //resetControl();
        filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap=filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);

    }

    private void resetControl() {
        if (editImageFragment != null){
            editImageFragment.resetControls();
        }
        brightnessFinal=0;
        saturationFinal=1.0f;
        constrantFinal=1.0f;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_save){
            saveImagetoGallery();
            Toast.makeText(this, "Save button is Clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void openCamera() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted())
                        {
                            ContentValues contentValue=new ContentValues();
                            contentValue.put(MediaStore.Images.Media.TITLE,"New Picture");
                            contentValue.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
                            uri_img_selected=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValue);
                            Intent inten=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            inten.putExtra(MediaStore.EXTRA_OUTPUT,uri_img_selected);
                            startActivityForResult(inten,CAMERA_REQUEST_CODE);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Permission denied",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void saveImagetoGallery()
        {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted())
                            {
                               photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                   @Override
                                   public void onBitmapReady(Bitmap savebitmap) {
                                       try {

                                           photoEditorView.getSource().setImageBitmap(savebitmap);

                                           final String path=BitMapUtils.insertImage(getContentResolver(),
                                                   savebitmap,System.currentTimeMillis()+"_profile.jpg",
                                                   null);
                                           if (!TextUtils.isEmpty(path))
                                           {
                                               Snackbar snackbar=Snackbar.make(coordinatorLayout,"Image saved",Snackbar.LENGTH_LONG)
                                                       .setAction("OPEN", new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               openImage(path);
                                                           }
                                                       });
                                               snackbar.show();
                                           }
                                           else
                                           {
                                               Snackbar snackbar=Snackbar.make(coordinatorLayout,"Error, can't save",Snackbar.LENGTH_LONG);
                                               snackbar.show();
                                           }

                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }
                                   }

                                   @Override
                                   public void onFailure(Exception e) {

                                   }
                               });


                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Permission denied",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                        }
                    }).check();

        }


    private void openImage(String path) {
        Intent intent =new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);

    }

    private void openImageFromGallery() {
        Dexter.withActivity(MainActivity.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted())
                        {
                            Intent i = new Intent(Intent.ACTION_PICK);
                            i.setType("image/*");


                            // pass the constant to compare it
                            // with the returned requestCode
                            startActivityForResult(i, PERMISSION_PICK_IMAGE);
                        }
                        else
                            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (resultCode == RESULT_OK){

                if (requestCode==PERMISSION_PICK_IMAGE) {
                    Bitmap bitmap = BitMapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

                    uri_img_selected=data.getData();
                    //clear bitmap memory
                    originalBitmap.recycle();
                    finalBitmap.recycle();
                    filteredBitmap.recycle();

                    originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    photoEditorView.getSource().setImageBitmap(originalBitmap);
                    bitmap.recycle();


                    filtersListFragment =FiltersListFragment.getInstance(originalBitmap);
                    filtersListFragment.setListener(this);

                }
                else if (requestCode==CAMERA_REQUEST_CODE) {
                    Bitmap bitmap = BitMapUtils.getBitmapFromGallery(this, uri_img_selected, 800, 800);


                    //clear bitmap memory
                    originalBitmap.recycle();
                    finalBitmap.recycle();
                    filteredBitmap.recycle();

                    originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    photoEditorView.getSource().setImageBitmap(originalBitmap);
                    bitmap.recycle();


                    filtersListFragment =FiltersListFragment.getInstance(originalBitmap);
                    filtersListFragment.setListener(this);

                }
                else if (requestCode==PERMISSION_INSERT_IMAGE)
                {

                    Bitmap bitmap=BitMapUtils.getBitmapFromGallery(this,data.getData(),250,250);
                    photoEditor.addImage(bitmap);

                }
                else if (requestCode==UCrop.REQUEST_CROP)
                {
                    CropResult(data);
                }
                else if (requestCode==UCrop.RESULT_ERROR)
                {
                    CropError(data);
                    
                }
            }
    }

    private void CropError(Intent data) {
        final Throwable cropError=UCrop.getError(data);
        if (cropError==null)
        {
            Toast.makeText(this, ""+cropError.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void CropResult(Intent data) {
        final Uri resultUri=UCrop.getOutput(data);
        if (resultUri!=null)
        {
            photoEditorView.getSource().setImageURI(resultUri);
            Bitmap bitmap=((BitmapDrawable) photoEditorView.getSource().getDrawable()).getBitmap();
            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filteredBitmap=originalBitmap;
            finalBitmap=originalBitmap;
        }
        else Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);

    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if (isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }

    @Override
    public void onAddTextButtonClick(String text, int color) {
        photoEditor.addText(text,color);
    }
}