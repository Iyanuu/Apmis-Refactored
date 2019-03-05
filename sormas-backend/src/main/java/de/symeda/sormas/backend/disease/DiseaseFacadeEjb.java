/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.backend.disease;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.disease.DiseaseBurdenDto;
import de.symeda.sormas.api.disease.DiseaseFacade;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.region.RegionReferenceDto;
import de.symeda.sormas.backend.caze.CaseFacadeEjb.CaseFacadeEjbLocal;
import de.symeda.sormas.backend.event.EventFacadeEjb.EventFacadeEjbLocal;
import de.symeda.sormas.backend.outbreak.OutbreakFacadeEjb.OutbreakFacadeEjbLocal;
import de.symeda.sormas.backend.person.PersonFacadeEjb.PersonFacadeEjbLocal;

/**
 * Provides the application configuration settings
 */
@Stateless(name="DiseaseFacade")
public class DiseaseFacadeEjb implements DiseaseFacade {
	
	@EJB
	private CaseFacadeEjbLocal caseFacade;
	
	@EJB
	private EventFacadeEjbLocal eventFacade;
	
	@EJB
	private OutbreakFacadeEjbLocal outbreakFacade;
	
	@EJB
	private PersonFacadeEjbLocal personFacade;
	
	//@Override
	public List<DiseaseBurdenDto> getDiseaseBurdenForDashboard(
			RegionReferenceDto regionRef,
			DistrictReferenceDto districtRef, 
			Date from, 
			Date to,
			Date previousFrom,
			Date previousTo,
			String userUuid) {
		
		//diseases
		List<Disease> diseases = Stream.of(Disease.values()).collect(Collectors.toList());
				
		//new cases
		CaseCriteria caseCriteria = new CaseCriteria()
				.newCaseDateBetween(from, to, null)
				.region(regionRef)
				.district(districtRef);
		
		Map<Disease, Long> newCases = caseFacade.getCaseCountPerDisease(caseCriteria, userUuid);
		
		//previous cases
		caseCriteria.newCaseDateBetween(previousFrom, previousTo, null);		
		Map<Disease, Long> previousCases = caseFacade.getCaseCountPerDisease(caseCriteria, userUuid);
		
		//events
		Map<Disease, Long> events = eventFacade.getEventCountByDisease(regionRef, districtRef, from, to);
					
		//outbreaks
		Map<Disease, Long> outbreaks = outbreakFacade.getOutbreakCountByDisease(regionRef, districtRef, from, to, userUuid);
						
		//case fatalities
		Map<Disease, Long> caseFatalities = personFacade.getDeathCountByDisease(regionRef, districtRef, from, to);
		
		//build diseasesBurden
		List<DiseaseBurdenDto> diseasesBurden = diseases.stream().map(disease -> {
			Long caseCount = newCases.getOrDefault(disease, 0L);
			Long previousCaseCount = previousCases.getOrDefault(disease, 0L);
			Long eventCount = events.getOrDefault(disease, 0L);
			Long outbreakDistrictCount = outbreaks.getOrDefault(disease, 0L);
			Long caseFatalityCount = caseFatalities.getOrDefault(disease, 0L);
			
			return new DiseaseBurdenDto(disease, caseCount, previousCaseCount, eventCount, outbreakDistrictCount, caseFatalityCount);
			
		}).collect(Collectors.toList());
		
		return diseasesBurden;
	}
		
	@LocalBean
	@Stateless
	public static class DiseaseFacadeEjbLocal extends DiseaseFacadeEjb {
	}
	
}