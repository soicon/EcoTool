import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/source">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Source
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/question">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Question
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/answer">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Answer
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/app-version">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;App Version
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/api-version">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Api Version
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/input-version">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Input Version
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/data-version">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Data Version
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/runner-log">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Runner Log
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/file-status">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;File Status
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
