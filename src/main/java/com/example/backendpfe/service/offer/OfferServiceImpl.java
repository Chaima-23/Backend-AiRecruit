package com.example.backendpfe.service.offer;

import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.offers.InternshipOffer;
import com.example.backendpfe.models.offers.JobOffer;
import com.example.backendpfe.models.offers.Offer;
import com.example.backendpfe.service.user.RecruiterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private static final Logger logger = LoggerFactory.getLogger(OfferServiceImpl.class);

    private final OfferRepository offerRepository;
    private final RecruiterService recruiterService;

    @Override
    @Transactional
    public Offer createJobOffer(JobOffer jobOffer) {
        if (jobOffer == null) {
            throw new IllegalArgumentException("Job offer cannot be null");
        }
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        jobOffer.setRecruiter(recruiter);
        Offer savedOffer = offerRepository.save(jobOffer);
        logger.info("Created job offer with ID: {}", savedOffer.getId());
        return savedOffer;
    }

    @Override
    @Transactional
    public Offer createInternshipOffer(InternshipOffer internshipOffer) {
        if (internshipOffer == null) {
            throw new IllegalArgumentException("Internship offer cannot be null");
        }
        if (internshipOffer.getStartDate() != null && internshipOffer.getEndDate() != null) {
            if (internshipOffer.getEndDate().isBefore(internshipOffer.getStartDate())) {
                throw new IllegalArgumentException("End date must be after start date for internship offer");
            }
        }
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        internshipOffer.setRecruiter(recruiter);
        Offer savedOffer = offerRepository.save(internshipOffer);
        logger.info("Created internship offer with ID: {}", savedOffer.getId());
        return savedOffer;
    }

    @Override
    public List<Offer> getOffersByCurrentRecruiter() {
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        List<Offer> offers = offerRepository.findByRecruiter(recruiter);
        logger.debug("Retrieved {} offers for recruiter with ID: {}", offers.size(), recruiter.getId());
        return offers;
    }

    @Override
    public Offer getOfferById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Offer ID cannot be null");
        }
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with ID: " + id));
        // Ne pas vérifier le recruteur ici, car cette méthode est utilisée dans des contextes publics et protégés
        logger.debug("Retrieved offer with ID: {}", id);
        return offer;
    }

    @Override
    @Transactional
    public Offer updateOffer(String id, Offer updatedOffer) {
        if (id == null || updatedOffer == null) {
            throw new IllegalArgumentException("Offer ID and updated offer cannot be null");
        }
        Offer existingOffer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with ID: " + id));
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        if (!existingOffer.getRecruiter().getId().equals(recruiter.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to offer with ID: " + id);
        }

        existingOffer.setField(updatedOffer.getField());
        existingOffer.setCountry(updatedOffer.getCountry());
        existingOffer.setCity(updatedOffer.getCity());
        existingOffer.setDescription(updatedOffer.getDescription());
        existingOffer.setMinQualifications(updatedOffer.getMinQualifications());
        existingOffer.setDutiesAndResponsibilities(updatedOffer.getDutiesAndResponsibilities());
        existingOffer.setTools(updatedOffer.getTools());
        existingOffer.setDeadline(updatedOffer.getDeadline());
        existingOffer.setSalary(updatedOffer.getSalary());
        existingOffer.setWorkMode(updatedOffer.getWorkMode());
        existingOffer.setStatus(updatedOffer.getStatus());

        if (existingOffer instanceof JobOffer jobOffer && updatedOffer instanceof JobOffer updatedJobOffer) {
            jobOffer.setSalary(updatedJobOffer.getSalary());
            jobOffer.setPosition(updatedJobOffer.getPosition());
            jobOffer.setWorkingHours(updatedJobOffer.getWorkingHours());
        } else if (existingOffer instanceof InternshipOffer internshipOffer && updatedOffer instanceof InternshipOffer updatedInternshipOffer) {
            LocalDate startDate = updatedInternshipOffer.getStartDate();
            LocalDate endDate = updatedInternshipOffer.getEndDate();
            if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("End date must be after start date for internship offer");
            }
            internshipOffer.setStartDate(startDate);
            internshipOffer.setEndDate(endDate);
        } else {
            throw new IllegalArgumentException("Mismatched offer types");
        }

        Offer savedOffer = offerRepository.save(existingOffer);
        logger.info("Updated offer with ID: {}", id);
        return savedOffer;
    }

    @Override
    @Transactional
    public void deleteOffer(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Offer ID cannot be null");
        }
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with ID: " + id));
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        if (!offer.getRecruiter().getId().equals(recruiter.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to offer with ID: " + id);
        }
        offerRepository.delete(offer);
        logger.info("Deleted offer with ID: {}", id);
    }

    @Override
    public List<Offer> getAllPublicOffers() {
        // Retourne uniquement les offres avec le statut "ACTIVE"
        return offerRepository.findByStatus("ACTIVE");
    }
}

// Exceptions personnalisées
class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException(String message) {
        super(message);
    }
}

class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}