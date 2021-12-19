package kg.geektech.mokerapi.ui.form;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import kg.geektech.mokerapi.App;
import kg.geektech.mokerapi.R;
import kg.geektech.mokerapi.data.models.Post;
import kg.geektech.mokerapi.databinding.FragmentFormBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormFragment extends Fragment {

    private FragmentFormBinding binding;
    private NavController navController;

    public FormFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormBinding.inflate(getLayoutInflater(), container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Post post = (Post) requireArguments().getSerializable("key");
            binding.etUserId.setText(String.valueOf(post.getUser()));
            binding.etDescription.setText(post.getContent());
            binding.etTitle.setText(post.getTitle());
        }
        binding.btnCreatePost.setOnClickListener(view1 -> {
            if (getArguments() != null) {
                updatePost();
            } else {
                createPost();
            }
        });
    }
    private Post getInformation(){
        Post post = new Post(
                binding.etTitle.getText().toString(),
                binding.etDescription.getText().toString(),
                Integer.parseInt(binding.etUserId.getText().toString()),35);
        return post;
    }

    private void createPost() {
        App.api.createPost(getInformation()).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                if (response.isSuccessful()) {
                    navController.navigateUp();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void updatePost() {
        App.api.update(getInformation().getId(), getInformation()).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NotNull Call<Post> call, @NotNull Response<Post> response) {
                if (response.isSuccessful()) {
                    navController.navigateUp();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Post> call, @NotNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}