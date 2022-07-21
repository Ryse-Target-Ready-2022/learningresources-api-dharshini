package com.tgt.rysetii.learningresourcesapi.service;

import com.tgt.rysetii.learningresourcesapi.entity.LearningResource;
import com.tgt.rysetii.learningresourcesapi.entity.LearningResourceStatus;
import com.tgt.rysetii.learningresourcesapi.repository.LearningResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LearningResourcesServiceTests {

    @Mock
    LearningResourceRepository learningResourceRepository;

    @InjectMocks
    LearningResourceService learningResourceService;

    @Test
    public void getProfitMarginsOfAllTheAvailableLearningResources() {
        List<LearningResource> learningResources = new ArrayList<>();
        LearningResource learningResource1 = new LearningResource(1311, "Test Name 1", 100.0, 150.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(5), LocalDate.now().plusYears(2));
        LearningResource learningResource2 = new LearningResource(1312, "Test Name 2", 150.0, 180.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(7), LocalDate.now().plusYears(3));
        learningResources.add(learningResource1);
        learningResources.add(learningResource2);

        List<Double> expectedProfitMargins = learningResources.stream().map(lr -> ((lr.getSellingPrice() - lr.getCostPrice())/lr.getSellingPrice())).collect(toList());

        when(learningResourceRepository.findAll()).thenReturn(learningResources);

        List<Double> actualProfitMargins = learningResourceService.getProfitMargin();
        assertEquals(expectedProfitMargins, actualProfitMargins, "Wrong Profit Margins");
    }

    @Test
    public void sortTheLearningResourceBasedOnProfitMarginInNonIncreasingOrder() {
        List<LearningResource> learningResources = new ArrayList<>();
        LearningResource learningResource1 = new LearningResource(1311, "Test Name 1", 100.0, 150.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(5), LocalDate.now().plusYears(2));
        LearningResource learningResource2 = new LearningResource(1312, "Test Name 2", 150.0, 180.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(7), LocalDate.now().plusYears(3));
        learningResources.add(learningResource1);
        learningResources.add(learningResource2);

        learningResources.sort((lr1,lr2) -> {
            Double profitMargin1 = (lr1.getSellingPrice() - lr1.getCostPrice())/ lr1.getSellingPrice();
            Double profitMargin2 = (lr2.getSellingPrice() - lr2.getCostPrice())/ lr2.getSellingPrice();
            return profitMargin2.compareTo(profitMargin1);
        });
        when(learningResourceRepository.findAll()).thenReturn(learningResources);

        List<LearningResource> learningResourcesSorted = learningResourceService.sortLearningResourcesByProfitMargin();

        assertEquals(learningResources, learningResourcesSorted, "The Learning Resources are not sorted by Profit Margin");
    }

    @Test
    public void saveTheLearningResources() {
        List<LearningResource> learningResources = new ArrayList<>();
        LearningResource learningResource1 = new LearningResource(1311, "Test Name 1", 100.0, 150.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(5), LocalDate.now().plusYears(2));
        LearningResource learningResource2 = new LearningResource(1312, "Test Name 2", 150.0, 180.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(7), LocalDate.now().plusYears(3));
        LearningResource learningResource3 = new LearningResource(1313, "Test Name 3", 150.0, 180.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(7), LocalDate.now().plusYears(3));

        learningResources.add(learningResource1);
        learningResources.add(learningResource2);
        learningResources.add(learningResource3);

        learningResourceService.saveLearningResources(learningResources);
        verify(learningResourceRepository, times(learningResources.size())).save(any(LearningResource.class));
    }

    @Test
    public void deleteLearningResourcesById() {
        int learningResourceId=1234;
        learningResourceService.deleteLearningResourceById(learningResourceId);

        verify(learningResourceRepository, times(1)).deleteById(learningResourceId);
    }

}
