package com.example.ejercicio3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ejercicio3.db.DatabaseHelper;
import com.example.ejercicio3.model.Member;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Images.Media.getBitmap;

public class SecondFragment extends Fragment {

    private final int SELECT_PHOTO = 1;
    private Uri uri;
    private ImageView imageView;
    private String imageUri;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();

            if (Build.VERSION.SDK_INT> Build.VERSION_CODES.JELLY_BEAN_MR2) {
                requireActivity().getContentResolver ().
                        takePersistableUriPermission (uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            //Convertir uri a string para poder salvar la imagen en la base de datos.
            imageUri = uri.toString();
            try {
                Bitmap bitmap = getBitmap(requireActivity().getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText edName = view.findViewById(R.id.edMemberNameAdd) ;
        final EditText edMatricula = view.findViewById(R.id.edMatriculaAdd);
        final EditText edAddress = view.findViewById(R.id.edAddressAdd);
        final EditText edEXpression = view.findViewById(R.id.edExpresionAdd);
        imageView = view.findViewById(R.id.imageViewMemberDetailAdd);

        final DatabaseHelper db = new DatabaseHelper(getContext());

        final Member member = new Member();

        //Pick image
        view.findViewById(R.id.imageViewMemberDetailAdd).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });


       //Save
        view.findViewById(R.id.btnAddMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member.setName(edName.getText().toString());
                member.setMatricula(edMatricula.getText().toString());
                member.setAddress(edAddress.getText().toString());
                member.setExpresion(edEXpression.getText().toString());
                member.setImage(imageUri);

                if(member.getName().isEmpty()){
                    Snackbar.make(view, "El nombre no puede estar vacio", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    if(db.insertOne(member) >= 0) {
                        Snackbar.make(view, "Registrado con exito", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        //Go to FrameList
                        //NavHostFragment.findNavController(SecondFragment.this)
                               // .navigate(R.id.action_SecondFragment_to_FrameList);

                        //Clear fields
                        edName.setText("");
                        edMatricula.setText("");
                        edAddress.setText("");
                        edEXpression.setText("");
                        imageUri="";
                        imageView.setImageResource(R.mipmap.ic_launcher);

                        //Back to last fragment
                        //NavHostFragment.findNavController(SecondFragment.this).popBackStack();

                        //Stay in this fragment
                        NavHostFragment.findNavController(SecondFragment.this)
                                .popBackStack(R.id.action_SecondFragment_to_FrameList,false);





                    }else{
                        Snackbar.make(view, "Ha ocurrido un error con la base de datos, no se ha completado la operacion", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }

            }
        });
    }
}
