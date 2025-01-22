package com.test.back.hesi.web.repos.mongo;

import com.test.back.hesi.web.model.doc.LoginHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepos extends MongoRepository<LoginHistory, String> {
}
