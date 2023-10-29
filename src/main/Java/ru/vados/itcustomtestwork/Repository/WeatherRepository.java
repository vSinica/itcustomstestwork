package ru.vados.itcustomtestwork.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vados.itcustomtestwork.Entity.WeatherEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    Optional<WeatherEntity> findFirstByCityOrderByPublishedDateDesc(String city);

    @Query(value = "select * from weathers w where w.city=:city order by abs(DATEDIFF(ss, w.published_date, :date )) limit 1 ",nativeQuery = true)
    WeatherEntity findByCityAndClosestDate(String city, Instant date );

}
