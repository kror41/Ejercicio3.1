package com.example.ejercicio3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FirstFragment extends Fragment {

    private final int SELECT_PHOTO = 2;
    private Uri uri;
    private ImageView imageView;
    private String imageUri;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView tvName = view.findViewById(R.id.tvMemberName);
        final EditText tvMatricula = view.findViewById(R.id.tvMatricula);
        final EditText tvAddress = view.findViewById(R.id.tvDirection);
        final EditText tvExpresion = view.findViewById(R.id.tvExpresion);
        imageView =  view.findViewById(R.id.imageViewMemberDetail);

        final Bundle bundle = this.getArguments();
        assert bundle != null;
        tvName.setText(bundle.getString("name"));
        tvMatricula.setText(bundle.getString("matricula"));
        tvAddress.setText(bundle.getString("address"));
        tvExpresion.setText(bundle.getString("expresion"));

        imageUri = bundle.getString("image");
        if(bundle.getString("image") != null){
            Uri uri = Uri.parse(bundle.getString("image"));
            try {
                Bitmap bitmap = getBitmap(requireActivity().getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            imageView.setImageResource(R.mipmap.ic_launcher);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });


        view.findViewById(R.id.btnDeleteMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
                if(databaseHelper.deleteOne(bundle.getString("name"))){
                    Snackbar.make(view, "Borrado exitoso", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_FrameList);
                }else {

                    Snackbar.make(view, "Se ha presentado un error con la base de datos, el miembro no fue borrado", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        view.findViewById(R.id.btnUpdateMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(v.getContext());
                Member member = new Member();
                member.setName(bundle.getString("name"));
                member.setMatricula(tvMatricula.getText().toString());
                member.setAddress(tvAddress.getText().toString());
                member.setExpresion(tvExpresion.getText().toString());
                member.setImage(imageUri);


                if(databaseHelper.Update(member)>=0){
                    Snackbar.make(v, "Actualizado exitoso", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_FrameList);
                }else {

                    Snackbar.make(v, "Se ha presentado un error con la base de datos, el miembro no fue actualizado", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }
}
