package com.sharity.sharityUser.fragment.sharity;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.fragment.Inscription1CallBack;
import com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerInscriptionAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.Y;


/**
 * Created by Moi on 14/11/15.
 */
public class Charity_Inscription1_fragment extends Fragment implements View.OnClickListener
{

    private EditText username;
    private EditText password;
    private Object byte_pictureFB;
    private static final int SELECT_PHOTO = 100;
    Bitmap yourSelectedImage;
    private ImageView done;
    private View inflate;
    public static boolean hasSelectLocation=false;
    private Double latitude;
    private Double longitude;
    private String type;
    private  CircleImageView profil;
    private Inscription1CallBack onSelect;

    public PagerInscriptionAdapter adapter;


    public static Charity_Inscription1_fragment newInstance(String type) {
        Charity_Inscription1_fragment myFragment = new Charity_Inscription1_fragment();
        Bundle args = new Bundle();
        args.putString("type",type);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_inscription1_charite, container, false);

        type=getArguments().get("type").toString();
        username=(EditText)inflate.findViewById(R.id.username);
        password=(EditText)inflate.findViewById(R.id.password);
        profil=(CircleImageView)inflate.findViewById(R.id.profil);
        done=(ImageView)inflate.findViewById(R.id.done);
        done.setOnClickListener(this);

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionRuntime permissionRuntime = new PermissionRuntime(getActivity());
                if (ContextCompat.checkSelfPermission(getActivity(),
                        permissionRuntime.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    GetPicture();
                }
                else {
                    permissionRuntime.Askpermission(permissionRuntime.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE, permissionRuntime.Code_WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        return inflate;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done:{
                if (username.getText().length()>0 && password.getText().length()>0){
                    Object[] fields={username,password,byte_pictureFB};
                    onSelect.OnSelector(fields);
               //     presenter.validateCredentials(type,fields,address);
                }else {
                    Toast.makeText(getActivity(),"Veuillez entrer un nom d'utilisateur et un mot de passe",Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void GetPicture(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        // check if parent Fragment implements listener
        if (getParentFragment() instanceof Inscription1CallBack) {
            onSelect = (Inscription1CallBack) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnSelection");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelect = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    new BackgroundTask(imageStream,selectedImage).execute();
                }
        }
    }


    private class BackgroundTask extends AsyncTask<String, Void, Void> {
        InputStream stream;
        Uri selectedImage;

        public BackgroundTask(InputStream stream, Uri selectedImage){
            this.stream=stream;
            this.selectedImage=selectedImage;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                stream = getActivity().getContentResolver().openInputStream(selectedImage);
                yourSelectedImage = BitmapFactory.decodeStream(stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            profil.setImageBitmap(yourSelectedImage);
            byte_pictureFB = DbBitmapUtility.getBytes(yourSelectedImage);
           /* Bitmap toto = convertToMutable(getContext(),yourSelectedImage);
            int [] allpixels = new int [toto.getHeight()*toto.getWidth()];

            toto.getPixels(allpixels, 0, toto.getWidth(), 0, 0, toto.getWidth(), toto.getHeight());

            for(int i = 0; i < allpixels.length; i++)
            {

                if (allpixels[i] >= 0xFF0f508f && allpixels[i] <= 0xFF204160)  {
                    allpixels[i] = 0xFF000000;
                }
                if (allpixels[i] >= 0xFF145d96 && allpixels[i] <= 0xFF204160)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF145d96 && allpixels[i] <= 0xFF204160)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF012045 && allpixels[i] <= 0xFF062133)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF0a5dae && allpixels[i] <= 0xFF0e60b2)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF0a5dae && allpixels[i] <= 0xFF0e60b2)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF0d599f && allpixels[i] <= 0xFF193553)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] == 0xFF0c2740)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF0c2740)  {
                    allpixels[i] = 0xFF000000;
                }
                if (allpixels[i] >= 0xFF1a4e7f)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF1a4e7f)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF2c71b2)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF2574bf)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF2d71b0)  {
                    allpixels[i] = 0xFF000000;
                }

                if (allpixels[i] >= 0xFF5890c9 && allpixels[i]<= 0xFF6293c4)  {
                    allpixels[i] = 0xFF000000;
                }


            toto.setPixels(allpixels, 0, toto.getWidth(), 0, 0, toto.getWidth(), toto.getHeight());
            profil.setImageBitmap(toto);
            convertBitmapToImage(toto);*/
            Log.d("UI thread", "I am the UI thread");
        }
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        public Bitmap convertToMutable(final Context context, final Bitmap imgIn) {
            final int width = imgIn.getWidth(), height = imgIn.getHeight();
            final Bitmap.Config type = imgIn.getConfig();
            File outputFile = null;
            final File outputDir = context.getCacheDir();
            try {
                outputFile = File.createTempFile(Long.toString(System.currentTimeMillis()), null, outputDir);
                outputFile.deleteOnExit();
                final RandomAccessFile randomAccessFile = new RandomAccessFile(outputFile, "rw");
                final FileChannel channel = randomAccessFile.getChannel();
                final MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
                imgIn.copyPixelsToBuffer(map);
                imgIn.recycle();
                final Bitmap result = Bitmap.createBitmap(width, height, type);
                map.position(0);
                result.copyPixelsFromBuffer(map);
                channel.close();
                randomAccessFile.close();
                outputFile.delete();
                return result;
            } catch (final Exception e) {
            } finally {
                if (outputFile != null)
                    outputFile.delete();
            }
            return null;
        }

        private void convertBitmapToImage(Bitmap bmp) {

            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Dummy");
            if (!root.exists()) {
                root.mkdirs();
            } else {
                System.out.print("Exists");
            }

            File f = new File(root, "filename.jpeg");
            //Convert bitmap to byte array
            Bitmap bitmap = bmp;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();


            try {

                f.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }



            //write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


