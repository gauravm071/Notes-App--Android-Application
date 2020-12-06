package com.example.notesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> implements Filterable {
    ArrayList<Notes> searchListOfNotes;
    ArrayList<Notes> listOfNotes;
    RecylerViewInterface recylerViewInterface;
    Context context = null;

    public NotesAdapter(RecylerViewInterface recylerViewInterface, Context applicationContext, @NonNull ArrayList<Notes> listOfNotes) {
        this.listOfNotes = listOfNotes;
        this.searchListOfNotes = new ArrayList<>(listOfNotes);
        this.recylerViewInterface = recylerViewInterface;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        holder.title.setText(listOfNotes.get(position).getTitle());
        holder.description.setText(listOfNotes.get(position).getDescription());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOfNotes.remove(position);
                Singleton.getInstance().getListOfNotes().remove(position);
                updateSharedPref();
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recylerViewInterface.onClick(getAdapterPosition());
                }

            });
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Notes> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(searchListOfNotes);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Notes notes : searchListOfNotes) {
                    if (notes.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(notes);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listOfNotes.clear();
            listOfNotes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void updateSharedPref() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Notes", Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("listOfNotes").apply();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String note = gson.toJson(Singleton.getInstance().getListOfNotes());
        editor.putString("listOfNotes", note).apply();
        editor.commit();
    }


}
