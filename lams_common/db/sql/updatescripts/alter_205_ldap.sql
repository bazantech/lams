-- database script to update 2.0.4 database with ldap support
update lams_configuration set config_value='2.0.5' WHERE config_key='version';
insert into lams_configuration (config_key, config_value) values ('LDAPProvisioningEnabled','true');
insert into lams_configuration (config_key, config_value) values ('LDAPProviderURL','ldap://192.168.111.15');
insert into lams_configuration (config_key, config_value) values ('LDAPSecurityAuthentication','simple');
insert into lams_configuration (config_key, config_value) values ('LDAPPrincipalDNPrefix','cn=');
insert into lams_configuration (config_key, config_value) values ('LDAPPrincipalDNSuffix',',ou=Users,dc=melcoe,dc=mq,dc=edu,dc=au');
insert into lams_configuration (config_key, config_value) values ('LDAPSecurityProtocol','');
insert into lams_configuration (config_key, config_value) values ('LDAPTruststorePath','');
insert into lams_configuration (config_key, config_value) values ('LDAPTruststorePassword','');
insert into lams_configuration (config_key, config_value) values ('LDAPLoginAttr','uid');
insert into lams_configuration (config_key, config_value) values ('LDAPFNameAttr','givenName');
insert into lams_configuration (config_key, config_value) values ('LDAPLNameAttr','sn');
insert into lams_configuration (config_key, config_value) values ('LDAPEmailAttr','mail');
insert into lams_configuration (config_key, config_value) values ('LDAPAddr1Attr','postalAddress');
insert into lams_configuration (config_key, config_value) values ('LDAPAddr2Attr','');
insert into lams_configuration (config_key, config_value) values ('LDAPAddr3Attr','');
insert into lams_configuration (config_key, config_value) values ('LDAPCityAttr','l');
insert into lams_configuration (config_key, config_value) values ('LDAPStateAttr','st');
insert into lams_configuration (config_key, config_value) values ('LDAPPostcodeAttr','postalCode');
insert into lams_configuration (config_key, config_value) values ('LDAPCountryAttr','');
insert into lams_configuration (config_key, config_value) values ('LDAPDayPhoneAttr','telephoneNumber');
insert into lams_configuration (config_key, config_value) values ('LDAPEveningPhoneAttr','homePhone');
insert into lams_configuration (config_key, config_value) values ('LDAPFaxAttr','facsimileTelephoneNumber');
insert into lams_configuration (config_key, config_value) values ('LDAPMobileAttr','mobile');
insert into lams_configuration (config_key, config_value) values ('LDAPLocaleAttr','preferredLanguage');
insert into lams_configuration (config_key, config_value) values ('LDAPDisabledAttr','!accountStatus');
insert into lams_configuration (config_key, config_value) values ('LDAPOrgAttr','deetITSchoolCode');
insert into lams_configuration (config_key, config_value) values ('LDAPRolesAttr','memberOf');
insert into lams_configuration (config_key, config_value) values ('LDAPLearnerMap','Student;SchoolSupportStaff;Teacher;SeniorStaff;Principal');
insert into lams_configuration (config_key, config_value) values ('LDAPMonitorMap','SchoolSupportStaff;Teacher;SeniorStaff;Principal');
insert into lams_configuration (config_key, config_value) values ('LDAPAuthorMap','Teacher;SeniorStaff;Principal');
insert into lams_configuration (config_key, config_value) values ('LDAPGroupAdminMap','Teacher;SeniorStaff');
insert into lams_configuration (config_key, config_value) values ('LDAPGroupManagerMap','Principal');
insert into lams_configuration (config_key, config_value) values ('LDAPUpdateOnLogin', 'true');
insert into lams_configuration (config_key, config_value) values ('LDAPOrgField', 'code');
insert into lams_configuration (config_key, config_value) values ('LDAPOnlyOneOrg', 'true');
insert into lams_configuration (config_key, config_value) values ('LDAPEncryptPasswordFromBrowser', 'false');
insert into lams_configuration (config_key, config_value) values ('LDAPSearchResultsPageSize', '100');