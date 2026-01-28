package com.example.urlshortener.service;

import com.example.urlshortener.domain.DatabaseSequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {
    private final MongoOperations mongoOperations;

    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
    public long generateSequence(String seqName){

        Query query = new Query(Criteria.where("_id").is(seqName));

        Update update = new Update().inc("seq", 1);

        FindAndModifyOptions options =
                FindAndModifyOptions.options()
                        .returnNew(true)
                        .upsert(true);

        DatabaseSequence counter = mongoOperations.findAndModify(
                query,
                update,
                options,
                DatabaseSequence.class
        );

        return counter.getSeq();
    }
}
