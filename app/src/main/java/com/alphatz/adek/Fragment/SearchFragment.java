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

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.R;

public class SearchFragment extends Fragment {
    private ImageView makananBerat, minumanSehat, desert;
    private ImageView kardio, kekuatan, interval;
    private ImageView tampilkanLebih;
    private ImageView cardViewImage1, cardViewImage2; // Dua ImageView untuk CardView

    private Handler handler = new Handler();
    private final int[] artikelImages = {R.drawable.gambar_kosong, R.drawable.gambar_kosong}; // Array gambar untuk artikel
    private int currentImageIndex = 0;

    private final int[] cardViewImages = {R.drawable.sandwich_bayam, R.drawable.dada_ayam}; // Array gambar untuk CardView
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

        //inisialisasi biasa
        makananBerat = view.findViewById(R.id.makanan_berat);
        minumanSehat = view.findViewById(R.id.minuman_sehat);
        desert = view.findViewById(R.id.desert);
        kardio = view.findViewById(R.id.kardio);
        kekuatan = view.findViewById(R.id.kekuatan);
        interval = view.findViewById(R.id.interval);
        tampilkanLebih = view.findViewById(R.id.tampilkan_lebih);
        cardViewImage1 = view.findViewById(R.id.imageView1);
        cardViewImage2 = view.findViewById(R.id.imageView2);


        TextView textResep = view.findViewById(R.id.textResep);
        TextView textOlahraga = view.findViewById(R.id.textOlahraga);

        //nambahin listener buat textResep sama textOlahraga (ini kudunya-
        //di text selengkapnya )
        textResep.setOnClickListener(v -> openResepFragment());
        textOlahraga.setOnClickListener(v -> openOlahragaFragment());


        //ini ga terlalu penting nanti diganti sama data API
        setupClickListeners();
        setupArtikelInfo(view, R.id.artikel1, "Meningkatkan Laju Metabolisme", "Kesehatan", "60", "120");
        setupArtikelInfo(view, R.id.artikel2, "Panduan Makanan Sehat", "Gaya Hidup", "45", "100");

        startCardViewImageRotation();
    }

    //buat pindah ke OlahragaFragment
    private void openOlahragaFragment() {
        Log.d("SearchFragment", "openOlahragaFragment called");

        // ini buat ngehide bottom navigation nya
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation();
        }

        // transaksi fragment ke OlahragaFragment (intinya buat pindah ke fragment lain)
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new OlahragaFragment());
        transaction.addToBackStack("SearchFragment");
        transaction.commit();

        Log.d("SearchFragment", "Fragment transaction committed to OlahragaFragment");
    }

    private void setupClickListeners() {
        makananBerat.setOnClickListener(v -> {
            showRoast();
            openResepFragment();
        });
    }
    //ini sama de kaya openOlahragaFragment (tapi ini masih blm ky openOlahragaFragement)
    private void openResepFragment() {
        Log.d("SearchFragment", "openResepFragment called");
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResepFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        Log.d("SearchFragment", "Fragment transaction committed");
    }

    private void showRoast() {
        String roastMessage = "Makanan berat? Siapa bilang diet itu mudah!";
        Toast.makeText(getActivity(), roastMessage, Toast.LENGTH_SHORT).show();
    }

    private void setupArtikelInfo(View parentView, int artikelLayoutId, String judul, String kategori, String likeCount, String viewCount) {
        View artikelView = parentView.findViewById(artikelLayoutId);

        TextView judulArtikel = artikelView.findViewById(R.id.judulArtikel);
        ImageView gambarArtikel = artikelView.findViewById(R.id.gambarArtikel);
        TextView kategoriArtikel = artikelView.findViewById(R.id.kategoriArtikel);
        TextView likeCountArtikel = artikelView.findViewById(R.id.likeCountArtikel);
        TextView viewCountArtikel = artikelView.findViewById(R.id.viewCountArtikel);

        judulArtikel.setText(judul);
        kategoriArtikel.setText(kategori);
        likeCountArtikel.setText(likeCount);
        viewCountArtikel.setText(viewCount);

        gambarArtikel.setImageResource(artikelImages[currentImageIndex]);

        Runnable imageChanger = new Runnable() {
            @Override
            public void run() {
                currentImageIndex = (currentImageIndex + 1) % artikelImages.length;
                gambarArtikel.setImageResource(artikelImages[currentImageIndex]);
                handler.postDelayed(this, 5000);
            }
        };

        handler.post(imageChanger);
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
        // Get the currently visible image and the next image
        ImageView currentImageView = isImage1Visible ? cardViewImage1 : cardViewImage2;
        ImageView nextImageView = isImage1Visible ? cardViewImage2 : cardViewImage1;

        // Set the next image resource
        currentCardViewImageIndex = (currentCardViewImageIndex + 1) % cardViewImages.length;
        nextImageView.setImageResource(cardViewImages[currentCardViewImageIndex]);
        nextImageView.setVisibility(View.VISIBLE); // Make the next image visible

        // Create slide out animation for the current image
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
