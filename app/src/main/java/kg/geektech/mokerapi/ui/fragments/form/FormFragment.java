package kg.geektech.mokerapi.ui.fragments.form;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import kg.geektech.mokerapi.App;
import kg.geektech.mokerapi.base.BaseFragment;
import kg.geektech.mokerapi.data.models.Post;
import kg.geektech.mokerapi.databinding.FragmentFormBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormFragment extends BaseFragment<FragmentFormBinding> {

    @Override
    protected FragmentFormBinding bind() {
        return FragmentFormBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupLogic() {
        if (getArguments() != null) {
            Post post = (Post) requireArguments().getSerializable("key");
            binding.etUserId.setText(String.valueOf(post.getUser()));
            binding.etDescription.setText(post.getContent());
            binding.etTitle.setText(post.getTitle());
        }
        binding.btnCreatePost.setOnClickListener(view1 -> {
            String title = binding.etTitle.getText().toString().trim();
            String content = binding.etDescription.getText().toString().trim();

            if (getArguments() != null) {
                Post post = (Post) requireArguments().getSerializable("key");
                post.setTitle(title);
                post.setContent(content);
                updatePost(post);
            } else {
                Post post = new Post(
                        binding.etTitle.getText().toString(),
                        binding.etDescription.getText().toString(),
                        Integer.parseInt(binding.etUserId.getText().toString()), 35);
                createPost(post);
            }
        });
    }

    @Override
    protected void setupUI() {

    }

    private void createPost(Post post) {
        App.api.createPost(post).enqueue(new Callback<Post>() {
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

    private void updatePost(Post post) {
        App.api.update(post.getId(), post).enqueue(new Callback<Post>() {
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