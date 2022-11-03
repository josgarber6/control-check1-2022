package org.springframework.samples.petclinic.feeding;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedingService {

    private FeedingRepository feedingRepository;

    @Autowired
    public FeedingService(FeedingRepository feedingRepository) {
        this.feedingRepository = feedingRepository;
    }

    @Transactional(readOnly = true)
    public List<Feeding> getAll(){
        return feedingRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<FeedingType> getAllFeedingTypes(){
        return feedingRepository.findAllFeedingTypes();
    }

    @Transactional(readOnly = true)
    public FeedingType getFeedingType(String typeName) {
        return feedingRepository.findTypeByName(typeName);
    }
    
    // @Transactional(readOnly = true)
    // public FeedingType getFeedingTypeById(Integer id) {
    //     return feedingRepository.findTypeById(id);
    // }

    @Transactional(rollbackFor = UnfeasibleFeedingException.class)
    public Feeding save(Feeding p) throws UnfeasibleFeedingException {
        Feeding f = feedingRepository.save(p);
        if (f != null) {
            return feedingRepository.save(p);  
        } else {
            throw new UnfeasibleFeedingException();
        }
    }

    
}
