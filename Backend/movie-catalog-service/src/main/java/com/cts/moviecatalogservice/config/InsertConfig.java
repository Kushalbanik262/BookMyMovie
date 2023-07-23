package com.cts.moviecatalogservice.config;

import com.cts.moviecatalogservice.model.Movie;
import com.cts.moviecatalogservice.model.Showing;
import com.cts.moviecatalogservice.model.Theater;
import com.cts.moviecatalogservice.repository.MovieRepository;
import com.cts.moviecatalogservice.repository.ShowingRepository;
import com.cts.moviecatalogservice.repository.TheaterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Slf4j
public class InsertConfig implements CommandLineRunner {
    @Autowired
    private MovieRepository mRepository;

    @Autowired
    private ShowingRepository sRepository;

    @Autowired
    private TheaterRepository tRepository;

    @Override
    public void run(String... args) throws Exception {


        Movie movie1 = Movie.builder().id("M28d95dfe-974c-4462-bc40-44650c185cf6").genre("Action,Adventure,Comedy,Scifi").cast("Chris Pratt, Zoe Saldana, Dave Baustita, Karen Gillan, Pom Klementieff, Vin Diesel")
                .posterUrl("https://assets-in.bmscdn.com/iedb/movies/images/mobile/thumbnail/xlarge/guardians-of-the-galaxy-vol-3-et00310794-1683529214.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=LH7KPMsN7nw&t=6s").rating("9.3").country("USA").director("James Gunn")
                .title("Guardians of Galaxy Vol. 3").language("English")
                .description("Our beloved band of misfits are settling into life on Knowhere. But it isn't long before their lives are upended by the echoes of Rocket's turbulent past. Peter Quill, still reeling from the loss of Gamora, must rally his team around him on a dangerous mission to save Rocket's life - a mission that, if not completed successfully, could quite possibly lead to the end of the Guardians as we know them.")
                .releaseDate(LocalDate.of(2023,05,05))
                .runtime(150)
                .build();

        Movie movie2 = Movie.builder().id("M3c056f1b-d2b8-4f3e-9a6b-f0ca0e8e51af").genre("Action, Adventure, Sci-Fi").cast("Chadwick Boseman, Michael B. Jordan, Lupita Nyong  Danai Gurira, Martin Freeman")
                .posterUrl("https://m.media-amazon.com/images/I/91ivR8RdFgL._SL1500_.jpg")
                .trailerUrl("https://www.youtube.com/watch?v=xjDjIWPwcPU")
                .rating("8.0")
                .country("USA")
                .director("Ryan Coogler")
                .title("Black Panther")
                .language("English")
                .runtime(134)
                .description("After the death of his father Challa returns home to the African nation of Wakanda to take his rightful place as king")
                .releaseDate(LocalDate.of(2023,12,10)).build();



        Theater theater1 = Theater.builder().id("T2b764eb8-7b5c-4e86-a119-d10734dfcc77").name("Cinepolis: Acropolis Mall, Kolkata").location("3rd Floor, Acropolis Mall 1858 Rajdanga Main Road, Beside Geetanjali stadium, Kolkata, West Bengal 700107, India").build();
        Theater theater2 = Theater.builder().id("Tb38ca66e-f538-4cbc-bd9f-f8365c0b2213").name("Hind INOX: Kolkata").location("76, Ganesh Chandra Avenue, Bow Bazaar, Dharmatala, Near Chandni Chowk Metro Station, Kolkata, West Bengal 700013, India").build();

        Showing show1 = Showing.builder().id("MT1565cd96-3fd2-4f0e-a203-485abdfb2937").showTime("7:15PM").movie(movie2).theater(theater1).bookedSeats(0).totalSeats(110).build();
        Showing show2 = Showing.builder().id("MT44dcc53d-a33e-42b1-ad81-17f03d2774a5").showTime("2:00PM").movie(movie2).theater(theater2).bookedSeats(0).totalSeats(70).build();


        movie2.setShows(List.of(show1,show2));

        theater1.setShows(List.of(show1));
        theater2.setShows(List.of(show2));

        mRepository.save(movie1);
        mRepository.save(movie2);

        tRepository.save(theater1);
        tRepository.save(theater2);


        sRepository.save(show1);
        sRepository.save(show2);

        log.debug("All Data Saved Successfully");
    }
}
