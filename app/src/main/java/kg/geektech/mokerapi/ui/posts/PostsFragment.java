package kg.geektech.mokerapi.ui.posts;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kg.geektech.mokerapi.App;
import kg.geektech.mokerapi.R;
import kg.geektech.mokerapi.data.models.Post;
import kg.geektech.mokerapi.databinding.FragmentPostsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsFragment extends Fragment {

    private FragmentPostsBinding binding;
    private NavController navController;
    private PostAdapter adapter;

    public PostsFragment() {
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PostAdapter();
        adapter.setOnClick(new PostAdapter.onClick() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                Post post = adapter.getItem(position);
                bundle.putSerializable("key",post);
                NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
                navController.navigate(R.id.action_postsFragment_to_formFragment,bundle);
            }
            @Override
            public void onLongClick(int position) {
                AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                alert.setTitle("Attention !!").setMessage("delete item ?").setPositiveButton("yes", (dialog, which)
                        -> App.api.deletePost(adapter.getItem(position).getId()).enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                        if (response.isSuccessful()){
                            adapter.removeItem(position);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {

                    }
                })).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
            }
        });
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostsBinding.inflate(getLayoutInflater(),container,false);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fab.setOnClickListener(v -> navController.navigate(R.id.action_postsFragment_to_formFragment));
        binding.recyclerView.setAdapter(adapter);

        App.api.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NotNull Call<List<Post>> call, @NotNull Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null){
                    adapter.setList(response.body());
                }
            }
            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}