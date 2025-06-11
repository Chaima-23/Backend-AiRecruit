package com.example.backendpfe.service.offer;

import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.offers.*;
import com.example.backendpfe.repositories.OfferRepository;
import com.example.backendpfe.service.user.RecruiterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private static final Logger logger = LoggerFactory.getLogger(OfferServiceImpl.class);

    private final OfferRepository offerRepository;
    private final RecruiterService recruiterService;

    // Crée une offre d'emploi full-time avec validation
    @Override
    @Transactional
    public Offer createFullTimeJob(FullTimeJob fullTimeJob) {
        return createJobOfferWithValidation(fullTimeJob);
    }

    // Crée une offre d'emploi part-time avec validation
    @Override
    @Transactional
    public Offer createPartTimeJob(PartTimeJob partTimeJob) {
        return createJobOfferWithValidation(partTimeJob);
    }

    // Méthode utilitaire privée pour valider et créer une offre d'emploi (full-time ou part-time)
    private Offer createJobOfferWithValidation(JobOffer jobOffer) {
        if (jobOffer == null) {
            throw new IllegalArgumentException("Job offer cannot be null");
        }
        if (jobOffer.getPosition() == null || jobOffer.getPosition().trim().isEmpty()) {
            throw new IllegalArgumentException("Position cannot be null or empty");
        }
        if (jobOffer.getWorkingHours() <= 0) {
            throw new IllegalArgumentException("Working hours must be greater than 0");
        }

        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        if (recruiter == null) {
            throw new IllegalStateException("No authenticated recruiter found");
        }

        jobOffer.setRecruiter(recruiter);
        Offer savedOffer = offerRepository.save(jobOffer);
        logger.info("Created {} job offer with ID: {}", jobOffer.getClass().getSimpleName(), savedOffer.getId());
        return savedOffer;
    }

    // Crée une offre de stage avec validation
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

    // Récupère toutes les offres du recruteur actuel
    @Override
    public List<Offer> getOffersByCurrentRecruiter() {
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        List<Offer> offers = offerRepository.findByRecruiter(recruiter);
        logger.debug("Retrieved {} offers for recruiter with ID: {}", offers.size(), recruiter.getId());
        return offers;
    }

    // Récupère une offre spécifique par son ID
    @Override
    public Offer getOfferById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Offer ID cannot be null");
        }
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with ID: " + id));
        logger.debug("Retrieved offer with ID: {}", id);
        return offer;
    }

    // Met à jour une offre d'emploi full-time
    @Override
    @Transactional
    public Offer updateFullTimeJob(String id, FullTimeJob updatedOffer) {
        return updateSpecificJob(id, updatedOffer, FullTimeJob.class);
    }

    // Met à jour une offre d'emploi part-time
    @Override
    @Transactional
    public Offer updatePartTimeJob(String id, PartTimeJob updatedOffer) {
        return updateSpecificJob(id, updatedOffer, PartTimeJob.class);
    }

    // Met à jour une offre de stage
    @Override
    @Transactional
    public Offer updateInternshipOffer(String id, InternshipOffer updatedOffer) {
        if (updatedOffer.getStartDate() != null && updatedOffer.getEndDate() != null &&
                updatedOffer.getEndDate().isBefore(updatedOffer.getStartDate())) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
        }
        return updateSpecificJob(id, updatedOffer, InternshipOffer.class);
    }

    // Méthode utilitaire privée pour mettre à jour une offre spécifique
    private <T extends Offer> Offer updateSpecificJob(String id, T updatedOffer, Class<T> expectedClass) {
        if (id == null || updatedOffer == null) {
            throw new IllegalArgumentException("ID de l'offre et contenu de l'offre ne doivent pas être null");
        }

        Offer existingOffer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Aucune offre trouvée avec l'ID : " + id));

        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        if (!existingOffer.getRecruiter().getId().equals(recruiter.getId())) {
            throw new UnauthorizedAccessException("Accès non autorisé à l'offre avec l'ID : " + id);
        }

        if (!expectedClass.isInstance(existingOffer)) {
            throw new IllegalArgumentException("Le type de l'offre existante ne correspond pas au type attendu.");
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

        if (existingOffer instanceof FullTimeJob fullTimeJob && updatedOffer instanceof FullTimeJob updatedFullTime) {
            fullTimeJob.setPosition(updatedFullTime.getPosition());
            fullTimeJob.setWorkingHours(updatedFullTime.getWorkingHours());
        } else if (existingOffer instanceof PartTimeJob partTimeJob && updatedOffer instanceof PartTimeJob updatedPartTime) {
            partTimeJob.setPosition(updatedPartTime.getPosition());
            partTimeJob.setWorkingHours(updatedPartTime.getWorkingHours());
        } else if (existingOffer instanceof InternshipOffer internshipOffer && updatedOffer instanceof InternshipOffer updatedInternship) {
            internshipOffer.setStartDate(updatedInternship.getStartDate());
            internshipOffer.setEndDate(updatedInternship.getEndDate());
        }

        Offer savedOffer = offerRepository.save(existingOffer);
        logger.info("Offre mise à jour avec l'ID : {}", id);
        return savedOffer;
    }

    // Supprime une offre générique
    @Override
    @Transactional
    public void deleteOffer(String id) {
        if (!offerRepository.existsById(id)) {
            throw new RuntimeException("Offer not found with id: " + id);
        }
        offerRepository.deleteById(id);
    }
    // Supprime une offre d'emploi full-time
    @Override
    @Transactional
    public void deleteFullTimeJob(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Offer ID cannot be null");
        }
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with ID: " + id));
        if (!(offer instanceof FullTimeJob)) {
            throw new IllegalArgumentException("Offer with ID " + id + " is not a FullTimeJob");
        }
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        if (!offer.getRecruiter().getId().equals(recruiter.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to offer with ID: " + id);
        }
        offerRepository.delete(offer);
        logger.info("Deleted full-time job offer with ID: {}", id);
    }

    // Supprime une offre d'emploi part-time
    @Override
    @Transactional
    public void deletePartTimeJob(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Offer ID cannot be null");
        }
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with ID: " + id));
        if (!(offer instanceof PartTimeJob)) {
            throw new IllegalArgumentException("Offer with ID " + id + " is not a PartTimeJob");
        }
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        if (!offer.getRecruiter().getId().equals(recruiter.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to offer with ID: " + id);
        }
        offerRepository.delete(offer);
        logger.info("Deleted part-time job offer with ID: {}", id);
    }

    // Supprime une offre de stage
    @Override
    @Transactional
    public void deleteInternshipOffer(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Offer ID cannot be null");
        }
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found with ID: " + id));
        if (!(offer instanceof InternshipOffer)) {
            throw new IllegalArgumentException("Offer with ID " + id + " is not an InternshipOffer");
        }
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        if (!offer.getRecruiter().getId().equals(recruiter.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to offer with ID: " + id);
        }
        offerRepository.delete(offer);
        logger.info("Deleted internship offer with ID: {}", id);
    }

    @Override
    public List<Offer> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        logger.debug("Retrieved {} offers for admin", offers.size());
        return offers;
    }
    // Récupère toutes les offres publiques avec statut "ACTIVE"
    @Override
    public List<Offer> getAllPublicOffers() {
        return offerRepository.findByStatus("ACTIVE");
    }

    // Récupère toutes les offres full-time du recruteur actuel
    @Override
    public List<FullTimeJob> getFullTimeOffersByCurrentRecruiter() {
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        List<FullTimeJob> offers = offerRepository.findFullTimeJobsByRecruiter(recruiter);
        logger.debug("Retrieved {} full-time offers for recruiter with ID: {}", offers.size(), recruiter.getId());
        return offers;
    }

    // Récupère toutes les offres part-time du recruteur actuel
    @Override
    public List<PartTimeJob> getPartTimeOffersByCurrentRecruiter() {
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        List<PartTimeJob> offers = offerRepository.findPartTimeJobsByRecruiter(recruiter);
        logger.debug("Retrieved {} part-time offers for recruiter with ID: {}", offers.size(), recruiter.getId());
        return offers;
    }

    // Récupère toutes les offres de stage du recruteur actuel
    @Override
    public List<InternshipOffer> getInternshipOffersByCurrentRecruiter() {
        Recruiter recruiter = recruiterService.getCurrentRecruiter();
        List<InternshipOffer> offers = offerRepository.findInternshipOffersByRecruiter(recruiter);
        logger.debug("Retrieved {} internship offers for recruiter with ID: {}", offers.size(), recruiter.getId());
        return offers;
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