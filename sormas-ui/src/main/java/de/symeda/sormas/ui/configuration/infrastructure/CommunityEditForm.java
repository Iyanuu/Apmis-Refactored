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
package de.symeda.sormas.ui.configuration.infrastructure;

import static de.symeda.sormas.ui.utils.LayoutUtil.fluidRowLocs;
import static de.symeda.sormas.ui.utils.LayoutUtil.loc;

import java.util.Locale;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.v7.data.validator.RegexpValidator;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.community.CommunityDto;
import de.symeda.sormas.api.infrastructure.district.DistrictDto;
import de.symeda.sormas.api.infrastructure.district.DistrictReferenceDto;
import de.symeda.sormas.api.infrastructure.region.RegionDto;
import de.symeda.sormas.api.infrastructure.region.RegionReferenceDto;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.NumberNumericValueValidator;

public class CommunityEditForm extends AbstractEditForm<CommunityDto> {

	private static final long serialVersionUID = 6726008587163831260L;

	private static final String HTML_LAYOUT =
			fluidRowLocs(CommunityDto.NAME, CommunityDto.CLUSTER_NUMBER, CommunityDto.EXTERNAL_ID_DUMMY ) + fluidRowLocs(CommunityDto.REGION, CommunityDto.DISTRICT);

	private boolean create;

	public CommunityEditForm(boolean create) {

		super(CommunityDto.class, CommunityDto.I18N_PREFIX, false);
		this.create = create;

		setWidth(540, Unit.PIXELS);

		if (create) {
			hideValidationUntilNextCommit();
		}
		addFields();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void addFields() {

		addField(CommunityDto.NAME, TextField.class);

		addField(CommunityDto.CLUSTER_NUMBER, TextField.class);
	
		TextField tfe = addField(CommunityDto.EXTERNAL_ID_DUMMY, TextField.class);
		tfe.addValidator( new RegexpValidator("^[0-9]\\d*$", "Not a valid input"));
		tfe.setCaption("CCode");

		ComboBox region = addInfrastructureField(CommunityDto.REGION);
		ComboBox district = addInfrastructureField(CommunityDto.DISTRICT);
		

		setRequired(true, CommunityDto.NAME, CommunityDto.NAME,CommunityDto.REGION, CommunityDto.DISTRICT, CommunityDto.EXTERNAL_ID_DUMMY);

		region.addValueChangeListener(e -> {
			RegionReferenceDto regionDto = (RegionReferenceDto) e.getProperty().getValue();
			FieldHelper
				.updateItems(district, regionDto != null ? FacadeProvider.getDistrictFacade().getAllActiveByRegion(regionDto.getUuid()) : null);
		});

		district.addValueChangeListener(e -> {
			if (e.getProperty().getValue() != null && region.getValue() == null) {
				DistrictDto communityDistrict =
					FacadeProvider.getDistrictFacade().getDistrictByUuid(((DistrictReferenceDto) e.getProperty().getValue()).getUuid());
				region.setValue(communityDistrict.getRegion());
			}
		});

		region.addItems(FacadeProvider.getRegionFacade().getAllActiveAsReference());

		// TODO: Workaround until cases and other data is properly transfered when infrastructure data changes
		if (!create) {
			region.setEnabled(false);
			district.setEnabled(false);
		}

	}

	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}
}
