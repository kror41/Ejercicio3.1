package com.example.ejercicio3;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ejercicio3.ListFragment.OnListFragmentInteractionListener;
import com.example.ejercicio3.model.Member;

import java.io.IOException;
import java.util.List;

import static android.provider.MediaStore.Images.Media.getBitmap;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Member> mValues;
    private final OnListFragmentInteractionListener mListener;

    MyItemRecyclerViewAdapter(List<Member> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

     //Set image
        if(mValues.get(position).getImage() != null) {
            Uri uri = Uri.parse(mValues.get(position).getImage());
            try {
                Bitmap bitmap = getBitmap(holder.mView.getContext().getContentResolver(), uri);
                holder.mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            holder.mImageView.setImageResource(R.mipmap.ic_launcher);
        }

        holder.mContentView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    Bundle bundle = new Bundle();
                    bundle.putString("name",mValues.get(position).getName());
                    bundle.putString("matricula",mValues.get(position).getMatricula());
                    bundle.putString("address",mValues.get(position).getAddress());
                    bundle.putString("expresion",mValues.get(position).getExpresion());
                    bundle.putString("image",mValues.get(position).getImage());

                    //Use Navigation because from adapter NavHostFragment it is not reached
                   Navigation.findNavController(v)
                            .navigate(R.id.action_FrameList_to_FirstFragment,bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        final ImageView mImageView;
        final TextView mContentView;
       private Member mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.imageViewMemberList);
            mContentView = view.findViewById(R.id.tvMemberNameList);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
