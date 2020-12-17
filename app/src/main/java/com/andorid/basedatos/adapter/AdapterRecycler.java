package com.andorid.basedatos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.basedatos.databinding.ItemListStudentBinding;
import com.andorid.basedatos.Modelo.Student;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.viewHolder> {

    private static final String TAG = AdapterRecycler.class.getSimpleName();

    private List<Student> miLista;
    private OnItemClickListener miListener;

    public AdapterRecycler() {
        this.miLista = new ArrayList<>();
    }

    @NonNull
    @Override
    public AdapterRecycler.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListStudentBinding binding = ItemListStudentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        final viewHolder holder = new viewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterRecycler.viewHolder holder, int position) {
        final Student item = miLista.get(position);
        holder.bind(item);
    }

    @Override
    public long getItemId(int position) {
        return miLista.get(position).getCodigo();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (miLista != null && miLista.size() > 0) {
            return miLista.size();
        } else {
            return 0;
        }
    }

    public void setOnListener(OnItemClickListener listener) {
        miListener = listener;
    }

    public void setData(List<Student> list) {
        if (list == null) list = new ArrayList<>();
        miLista.clear();
        this.miLista = list;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ItemListStudentBinding binding;

        public viewHolder(ItemListStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Student item) {
            binding.txtCode.setText(String.valueOf(item.getCodigo()));
            binding.txtNameStudent.setText(item.getNombre());
            binding.txtLastNameStudent.setText(item.getApellido());

            binding.containerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (miListener != null)
                        miListener.onItemClick(view, item, getAdapterPosition(), false);
                }
            });
        }
    }

    public interface OnItemClickListener {
        boolean onItemClick(View view, Student item, int position, boolean longPress);
    }
}