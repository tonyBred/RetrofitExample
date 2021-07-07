package com.example.retrofitexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textviewresult;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textviewresult = findViewById(R.id.text_view_result);

        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header", "Supp!")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //Call Retrofit from class Post
        //getPosts();

        //Call Retrofit from class Comment
        //getComments();

        //Send HTTP Request Post
        //createPost();

        updatePost();

        //deletePost();
    }

    private void getPosts() {

        //Use Posts with defined Parameter
        //Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,5,6}, null, null);

        //Use Posts with Map Parameter
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textviewresult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserID() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getTitle() + "\n\n";

                    textviewresult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textviewresult.setText(t.getMessage());
            }
        });
    }

    private void getComments() {

        //Comment with url in @GET
        //Call<List<Comment>> call = jsonPlaceHolderApi.getComments(3);

        //Comment with url in parameter
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments("posts/4/comments");

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if (!response.isSuccessful()) {
                    textviewresult.setText("Code: " + response.code());
                    return;
                }

                List<Comment> posts = response.body();

                for (Comment post : posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "Post ID: " + post.getPostId() + "\n";
                    content += "Name: " + post.getName() + "\n";
                    content += "Email: " + post.getEmail() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    textviewresult.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textviewresult.setText(t.getMessage());
            }
        });
    }

    private void createPost() {

        //Send Post with defined Data in class
        //Post post = new Post(23, "Hello ", "World!");
        //Call<Post> call = jsonPlaceHolderApi.createPost(post);

        //Send Post with FormUrlEncoded and defined Paramater
        //Call<Post> call = jsonPlaceHolderApi.createPost(23, "Hello ", "World!");

        //Send Post with FormUrlEncoded and Map Paramater
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "23");
        parameters.put("title", "Hello ");
        Call<Post> call = jsonPlaceHolderApi.createPost(parameters);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    textviewresult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserID() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                textviewresult.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textviewresult.setText(t.getMessage());
            }
        });
    }

    private void updatePost(){
        Post post = new Post(12, null, "Supp");

        //PutPost (will completly replace object)
        //Call<Post> call = jsonPlaceHolderApi.putPost("abc",5, post);

        //PatchPost (will not replace null variable in an object)
        Map<String, String> headers = new HashMap<>();
        headers.put("Map-Header1","Hello");
        headers.put("Map-Header2","Bandung");

        Call<Post> call = jsonPlaceHolderApi.patchPost(headers,5, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    textviewresult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();


                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserID() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                textviewresult.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textviewresult.setText(t.getMessage());
            }
        });
    }

    private void deletePost(){
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textviewresult.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textviewresult.setText(t.getMessage());
            }
        });
    }
}