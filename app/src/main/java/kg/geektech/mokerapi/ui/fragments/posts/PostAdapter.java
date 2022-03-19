package kg.geektech.mokerapi.ui.fragments.posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import kg.geektech.mokerapi.data.models.Post;
import kg.geektech.mokerapi.databinding.ItemListBinding;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ItemListBinding binding;
    private List<Post> list = new ArrayList<>();
    private onClick onClick;

    public void setOnClick(PostAdapter.onClick onClick) {
        this.onClick = onClick;
    }

    public void setList(List<Post> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }
    public Post getItem(int position){
        return list.get(position);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = ItemListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void bind(Post post) {
            binding.tvTitle.setText(post.getTitle());
            binding.tvDescription.setText(post.getContent());
            binding.tvUserID.setText(String.valueOf(post.getUser()));
            binding.btnUpgrade.setOnClickListener(v -> onClick.onItemClick(getAdapterPosition()));
            itemView.setOnLongClickListener(v -> {
                onClick.onLongClick(getAdapterPosition());
                return true;
            });
        }
    }
    interface onClick{
        void onItemClick(int position);
        void onLongClick(int position);
    }
}
