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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.api.infrastructure.community;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import de.symeda.sormas.api.EntityDto;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.FieldConstraints;

public class CommunityDto extends EntityDto {

	private static final long serialVersionUID = -8833267932522978860L;

	public static final String I18N_PREFIX = "Community";

	public static final String NAME = "name";
	public static final String GROWTH_RATE = "growthRate";
	public static final String REGION = "region";
	public static final String DISTRICT = "district";
	public static final String EXTERNAL_ID = "externalId";
	public static final String EXTERNAL_ID_DUMMY = "externalIddummy";
	public static final String CLUSTER_NUMBER="clusterNumber";

	public static final String AREA_NAME = "areaname";
	public static final String AREA_EXTERNAL_ID = "areaexternalId";
	public static final String REGION_EXTERNALID = "regionexternalId";
	public static final String DISTRICT_EXTERNALID = "districtexternalId";
	

	@Size(max = FieldConstraints.CHARACTER_LIMIT_SMALL, message = Validations.textTooLong)
	private String name;
	private Float growthRate;
	private RegionReferenceDto region;
	private DistrictReferenceDto district;
	private boolean archived;
	private Integer clusterNumber;
	private Long externalId;
	private String externalIddummy;
	private Long districtexternalId;
	private Long regionexternalId;
	private Long areaexternalId;
	private String areaname;
	

	public CommunityDto(
		Date creationDate,
		Date changeDate,
		String uuid,
		boolean archived,
		String name,
		Float growthRate,
		String regionUuid,
		String regionName,
		Long regionExternalId,
		String districtUuid,
		String districtName,
		Long districtExternalId,
		Long externalId,
		Integer clusterNumber) {

		super(creationDate, changeDate, uuid);
		this.archived = archived;
		this.name = name;
		this.growthRate = growthRate;
		this.region = new RegionReferenceDto(regionUuid, regionName, regionExternalId);
		this.district = new DistrictReferenceDto(districtUuid, districtName, districtExternalId);
		this.externalId = externalId;
		this.clusterNumber = clusterNumber;
	}

	public CommunityDto() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(Float growthRate) {
		this.growthRate = growthRate;
	}

	public RegionReferenceDto getRegion() {
		return region;
	}

	public void setRegion(RegionReferenceDto region) {
		this.region = region;
	}

	public DistrictReferenceDto getDistrict() {
		return district;
	}

	public void setDistrict(DistrictReferenceDto district) {
		this.district = district;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Long getExternalId() {
		return externalId;
	}
	
	

	public String getExternalIddummy() {
		
		if(externalId != null) {
			externalIddummy = externalId+"";
		}
		
		return externalIddummy;
	}

	public void setExternalIddummy(String externalIddummy) {
		
		if(externalIddummy != null) {
			this.externalId = Long.parseLong(externalIddummy);
		}
				
	}

	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}

	public Integer getClusterNumber() {
		return clusterNumber;
	}

	public void setClusterNumber(Integer clusterNumber) {
		this.clusterNumber = clusterNumber;
	}

	public CommunityReferenceDto toReference() {
		return new CommunityReferenceDto(getUuid(), getName(), getExternalId(), getClusterNumber());
	}
	
	public Long getDistrictexternalId() {
		return districtexternalId;
	}

	public void setDistrictexternalId(Long districtexternalId) {
		this.districtexternalId = districtexternalId;
	}

	public Long getRegionexternalId() {
		return regionexternalId;
	}

	public void setRegionexternalId(Long regionexternalId) {
		this.regionexternalId = regionexternalId;
	}

	public Long getAreaexternalId() {
		return areaexternalId;
	}

	public void setAreaexternalId(Long areaexternalId) {
		this.areaexternalId = areaexternalId;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	@Override
	public String toString() {
		return getName();
	}

	public static CommunityDto build() {
		CommunityDto dto = new CommunityDto();
		dto.setUuid(DataHelper.createUuid());
		return dto;
	}
}
