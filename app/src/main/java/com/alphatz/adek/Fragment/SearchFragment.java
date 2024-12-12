package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.Adapter.ArtikelAdapter;
import com.alphatz.adek.Model.ArtikelModel;
import com.alphatz.adek.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements ArtikelAdapter.OnArtikelClickListener {
    // UI Components
    private ImageView makananBerat, minumanSehat, desert;
    private ImageView kelenturan, kekuatan, interval;
    private ImageView tampilkanLebih;
    private ImageView cardViewImage1, cardViewImage2;
    private TextView selengkapnya_resep, selengkapnya_olahraga;

    // Artikel RecyclerView Components
    private RecyclerView recyclerViewArtikel;
    private ArtikelAdapter artikelAdapter;
    private List<ArtikelModel> artikelList;

    // Image Rotation
    private Handler handler = new Handler();
    private final int[] artikelImages = {R.drawable.gambar_kosong, R.drawable.gambar_kosong};
    private int currentImageIndex = 0;

    private final int[] cardViewImages = {R.drawable.sandwich_bayam, R.drawable.dada_ayam};
    private int currentCardViewImageIndex = 0;
    private boolean isImage1Visible = true;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize all views
        initializeViews(view);

        // Setup click listeners
        setupClickListeners();

        // Setup artikel recycler view
        setupArtikelRecyclerView();

        // Fetch artikel data
        fetchArtikelData();

        // Start image rotations
        startCardViewImageRotation();
    }

    private void initializeViews(View view) {
        // Initialize all UI components (similar to previous implementation)
        makananBerat = view.findViewById(R.id.makanan_berat);
        minumanSehat = view.findViewById(R.id.minuman_sehat);
        selengkapnya_olahraga = view.findViewById(R.id.selengkapnya_olahraga);
        selengkapnya_resep = view.findViewById(R.id.selengkapnya_resep);
        desert = view.findViewById(R.id.desert);
        kelenturan = view.findViewById(R.id.kardio);
        kekuatan = view.findViewById(R.id.kekuatan);
        interval = view.findViewById(R.id.interval);
//        tampilkanLebih = view.findViewById(R.id.tampilkan_lebih);
        cardViewImage1 = view.findViewById(R.id.imageView1);
        cardViewImage2 = view.findViewById(R.id.imageView2);

        // Artikel RecyclerView
        recyclerViewArtikel = view.findViewById(R.id.recyclerViewArtikel);
    }

    private void setupClickListeners() {
        makananBerat.setOnClickListener(v -> openMakananBeratFragment());
        minumanSehat.setOnClickListener(v -> openMinumanSehatFragment());
        desert.setOnClickListener(v -> openDessertFragment());
        kelenturan.setOnClickListener(v -> openKelenturanFragment());
        kekuatan.setOnClickListener(v -> openKekuatanFragment());

        selengkapnya_resep.setOnClickListener(v -> openResepFragment());
        selengkapnya_olahraga.setOnClickListener(v -> openOlahragaFragment());
    }

    private void setupArtikelRecyclerView() {
        recyclerViewArtikel.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        artikelList = new ArrayList<>();
        artikelAdapter = new ArtikelAdapter(
                artikelList,
                this,
                getParentFragmentManager()
        );

        recyclerViewArtikel.setAdapter(artikelAdapter);
    }

    private void fetchArtikelData() {
        String url = "http://adek-app.my.id/ads_mysql/search/get_artikel.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Log.d("FetchArtikelData", "Response received: " + response);

                        JSONObject jsonResponse = new JSONObject(response);
                        if ("success".equals(jsonResponse.getString("status"))) {
                            JSONArray artikelArray = jsonResponse.getJSONArray("data");

                            artikelList.clear(); // Bersihkan daftar artikel sebelum menambahkan data baru

                            Log.d("FetchArtikelData", "Jumlah artikel ditemukan: " + artikelArray.length());

                            for (int i = 0; i < artikelArray.length(); i++) {
                                JSONObject artikel = artikelArray.getJSONObject(i);

                                String judul = artikel.optString("judulArtikel", "");
                                String kategori = artikel.optString("kategori", "");
                                String gambarPath = artikel.optString("gambar", "");

                                Log.d("FetchArtikelData", "Artikel ke-" + i + ": Judul = " + judul + ", Kategori = " + kategori);

                                // Buat URL lengkap untuk gambar jika path tersedia
                                String gambarUrl = gambarPath.isEmpty() ? null : "http://adek-app.my.id/Images/" + gambarPath;

                                // Buat model ArtikelModel menggunakan URL gambar
                                ArtikelModel artikelModel = new ArtikelModel(judul, kategori, gambarUrl);
                                artikelList.add(artikelModel);
                            }

                            artikelAdapter.notifyDataSetChanged();
                            Log.d("FetchArtikelData", "Artikel list updated. Total: " + artikelList.size());
                        } else {
                            Toast.makeText(getContext(), "Gagal memuat artikel", Toast.LENGTH_SHORT).show();
                            Log.e("FetchArtikelData", "Server response status: " + jsonResponse.getString("status"));
                        }
                    } catch (JSONException e) {
                        Log.e("FetchArtikelData", "JSON Parsing Error: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("FetchArtikelData", "Volley Error: " + error.toString());
                    Toast.makeText(getContext(), "Error fetching articles", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
    @Override
    public void onArtikelClick(ArtikelModel artikel) {
        // Optional additional handling if needed
    }

    //    private void openKardioFragment(){
//        if (getActivity() instanceof  Dashboard){
//            ((Dashboard) getActivity()).hideBottomNavigation();
//        }
//    }

    private void openKekuatanFragment() {
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation();
        }

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new KekuatanFragment());
        transaction.addToBackStack("SearchFragment");
        transaction.commit();
    }

    private void openKelenturanFragment() {
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation();
        }

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new KardioFragment());
        transaction.addToBackStack("SearchFragment");
        transaction.commit();
    }

    // Pada bagian openResepFragment dan openOlahragaFragment, tambahkan ke BackStack dengan ID yang mudah dilacak.
    private void openResepFragment() {
        Log.d("SearchFragment", "openResepFragment called");

        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation();
        }
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResepFragment());
        transaction.addToBackStack("SearchFragment"); // Tambahkan dengan identitas yang konsisten
        transaction.commit();

        Log.d("SearchFragment", "Fragment transaction committed to ResepFragment");
    }

    private void openOlahragaFragment() {
        Log.d("SearchFragment", "openOlahragaFragment called");

        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation();
        }

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new OlahragaFragment());
        transaction.addToBackStack("SearchFragment");
        transaction.commit();

        Log.d("SearchFragment", "Fragment transaction committed to OlahragaFragment");
    }

    private void showRoast() {
        String roastMessage = "Makanan berat? Siapa bilang diet itu mudah!";
        Toast.makeText(getActivity(), roastMessage, Toast.LENGTH_SHORT).show();
    }

    // method untuk membuka fragment tapi ngilangin nav bar yg ada dibawah ( nge hide )
    private void openMakananBeratFragment() {
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation(); //hide
        }

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MakananBeratFragment());
        transaction.addToBackStack("SearchFragment");
        transaction.commit();
    }

    private void openMinumanSehatFragment() {
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation(); // Sembunyikan nav bottom
        }

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MinumanSehatFragment());
        transaction.addToBackStack("SearchFragment");
        transaction.commit();
    }

    private void openDessertFragment() {
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation(); // Sembunyikan nav bottom
        }

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new DessertFragment());
        transaction.addToBackStack("SearchFragment");
        transaction.commit();
    }


    private void startCardViewImageRotation() {
        Runnable imageChanger = new Runnable() {
            @Override
            public void run() {
                animateImageChange();
                handler.postDelayed(this, 5000); // Change image every 5 seconds
            }
        };

        handler.postDelayed(imageChanger, 2000); // Start after 2 seconds
    }

    private void animateImageChange() {
        ImageView currentImageView = isImage1Visible ? cardViewImage1 : cardViewImage2;
        ImageView nextImageView = isImage1Visible ? cardViewImage2 : cardViewImage1;

        currentCardViewImageIndex = (currentCardViewImageIndex + 1) % cardViewImages.length;
        nextImageView.setImageResource(cardViewImages[currentCardViewImageIndex]);
        nextImageView.setVisibility(View.VISIBLE); // Make the next image visible

        Animation slideOutRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 1f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f
        );
        slideOutRight.setDuration(500);

        // Create fade out animation
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(500);

        // Combine animations for slide out
        AnimationSet animationOut = new AnimationSet(true);
        animationOut.addAnimation(slideOutRight);
        animationOut.addAnimation(fadeOut);

        // Set listener to change the image immediately after slide out ends
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                currentImageView.setVisibility(View.GONE); // Hide current image after animation ends
                isImage1Visible = !isImage1Visible; // Toggle the image view visibility
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // Start the slide out animation
        currentImageView.startAnimation(animationOut);

        // Create slide in animation for the next image
        Animation slideInLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f
        );
        slideInLeft.setDuration(500);

        // Create fade in animation
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);

        // Combine animations for slide in
        AnimationSet animationIn = new AnimationSet(true);
        animationIn.addAnimation(slideInLeft);
        animationIn.addAnimation(fadeIn);

        // Start the slide in animation immediately after changing the image
        nextImageView.startAnimation(animationIn);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
