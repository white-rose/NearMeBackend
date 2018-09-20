package com.SmallTalk.LocationHandler;

import com.SmallTalk.model.Location.LocationTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<LocationTag, Long> { }
