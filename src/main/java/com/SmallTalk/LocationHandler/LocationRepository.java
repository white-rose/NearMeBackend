package com.SmallTalk.LocationHandler;

import com.SmallTalk.model.Location.LocationTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationTag, Long> {



}
