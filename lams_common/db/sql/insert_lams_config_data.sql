INSERT INTO lams_configuration VALUES
('AdminScreenSize','1280x720','config.admin.screen.size','config.header.look.feel','STRING',1),
('AllowDirectAccessIntgrtnLrnr','false','config.allow.direct.access.for.integration.learners','config.header.features','BOOLEAN',1),
('AllowDirectLessonLaunch','false','config.allow.direct.lesson.launch','config.header.features','BOOLEAN',1),
('AllowKumalive','true','config.allow.kumalive','config.header.features','BOOLEAN',1),
('AllowLiveEdit','true','config.allow.live.edit','config.header.features','BOOLEAN',1),
('AntivirusEnable','false','config.av.enable','config.header.antivirus','BOOLEAN',1),
('AntivirusHost','localhost','config.av.host','config.header.antivirus','STRING',0),
('AntivirusPort','3310','config.av.port','config.header.antivirus','LONG',0),
('AuthoringScreenSize','1280x720','config.authoring.screen.size','config.header.look.feel','STRING',1),
('CleanupPreviewOlderThanDays','7','config.cleanup.preview.older.than.days','config.header.system','LONG',1),
('ConfigCacheRefreshInterval','0','config.cache.refresh','config.header.system','LONG',0),
('ContentRepositoryPath','@contentrepository.directory@','config.content.repository.path','config.header.uploads','STRING',1),
('CustomTabLink','','config.custom.tab.link','config.header.look.feel','STRING',0),
('CustomTabTitle','','config.custom.tab.title','config.header.look.feel','STRING',0),
('DefaultTheme','defaultHTML','config.default.html.theme','config.header.look.feel','STRING',1),
('DictionaryDateCreated','2021-10-25','config.dictionary.date.created','config.header.versions','STRING',1),
('DisplayPortrait','true','config.display.portrait','config.header.look.feel','BOOLEAN',0),
('DisplayPrintButton','false','config.display.print.button','config.header.features','BOOLEAN',1),
('EARDir','@ear.directory@','config.ear.dir','config.header.system','STRING',1),
('EnableCollapsingSubcourses','false','config.enable.collapsing.subcourses','config.header.features','BOOLEAN',0),
('EnableForgotYourPasswordLink','true','config.enable.forgot.your.password.link','config.header.features','BOOLEAN',0),
('EnablePortraitEditing','true','config.enable.portrait.editing','config.header.features','BOOLEAN',0),
('EnableServerRegistration','false','config.server2server.registration.enable','config.header.system','BOOLEAN',1),
('ErrorStackTrace','true','config.stacktrace.error','config.header.system','BOOLEAN',0),
('EtherpadApiKey','','config.etherpad.api.key','config.header.etherpad','STRING',0),
('EtherpadInstanceID','LAMS','config.etherpad.instance.id','config.header.etherpad','STRING',0),
('EtherpadServerUrl','http://localhost:9001','config.etherpad.server.url','config.header.etherpad','STRING',0),
('ExecutableExtensions','.bat,.bin,.com,.cmd,.exe,.msi,.msp,.ocx,.pif,.scr,.sct,.sh,.shs,.vbs,.php,.jsp,.asp,.aspx,.pl,.do,.py,.tcl,.cgi,.shtml,.stm,.cfm,.adp','config.executable.extensions','config.header.uploads','STRING',1),
('FailedAttempts','3','config.failed.attempts','config.header.password.policy','LONG',1),
('HelpURL','http://wiki.lamsfoundation.org/display/lamsdocs/','config.help.url','config.header.system','STRING',1),
('LamsSupportEmail','','config.lams.support.email','config.header.email','STRING',0),
('LDAPAddr1Attr','postalAddress','admin.user.address_line_1','config.header.ldap.attributes','STRING',0),
('LDAPAddr2Attr','','admin.user.address_line_2','config.header.ldap.attributes','STRING',0),
('LDAPAddr3Attr','','admin.user.address_line_3','config.header.ldap.attributes','STRING',0),
('LDAPAuthorMap','Teacher;SeniorStaff;Principal','config.ldap.author.map','config.header.ldap.attributes','STRING',0),
('LDAPBaseDN','ou=Users,dc=melcoe,dc=mq,dc=edu,dc=au','config.ldap.base.dn','config.header.ldap','STRING',0),
('LDAPBindUserDN','','config.ldap.bind.user.dn','config.header.ldap','STRING',0),
('LDAPBindUserPassword','','config.ldap.bind.user.password','config.header.ldap','STRING',0),
('LDAPCityAttr','l','admin.user.city','config.header.ldap.attributes','STRING',0),
('LDAPCountryAttr','','admin.user.country','config.header.ldap.attributes','STRING',0),
('LDAPDayPhoneAttr','telephoneNumber','admin.user.day_phone','config.header.ldap.attributes','STRING',0),
('LDAPDisabledAttr','!accountStatus','sysadmin.disabled','config.header.ldap.attributes','STRING',0),
('LDAPEmailAttr','mail','admin.user.email','config.header.ldap.attributes','STRING',0),
('LDAPEveningPhoneAttr','homePhone','admin.user.evening_phone','config.header.ldap.attributes','STRING',0),
('LDAPFaxAttr','facsimileTelephoneNumber','admin.user.fax','config.header.ldap.attributes','STRING',0),
('LDAPFNameAttr','givenName','admin.user.first_name','config.header.ldap.attributes','STRING',0),
('LDAPGroupManagerMap','Principal;Teacher;SeniorStaff','config.ldap.group.manager.map','config.header.ldap.attributes','STRING',0),
('LDAPLearnerMap','Student;SchoolSupportStaff;Teacher;SeniorStaff;Principal','config.ldap.learner.map','config.header.ldap.attributes','STRING',0),
('LDAPLNameAttr','sn','admin.user.last_name','config.header.ldap.attributes','STRING',0),
('LDAPLocaleAttr','preferredLanguage','admin.organisation.locale','config.header.ldap.attributes','STRING',0),
('LDAPLoginAttr','uid','admin.user.login','config.header.ldap.attributes','STRING',0),
('LDAPMobileAttr','mobile','admin.user.mobile_phone','config.header.ldap.attributes','STRING',0),
('LDAPMonitorMap','SchoolSupportStaff;Teacher;SeniorStaff;Principal','config.ldap.monitor.map','config.header.ldap.attributes','STRING',0),
('LDAPOnlyOneOrg','true','config.ldap.only.one.org','config.header.ldap','BOOLEAN',1),
('LDAPOrgAttr','schoolCode','admin.course','config.header.ldap.attributes','STRING',0),
('LDAPOrgField','code','config.ldap.org.field','config.header.ldap.attributes','STRING',0),
('LDAPPostcodeAttr','postalCode','admin.user.postcode','config.header.ldap.attributes','STRING',0),
('LDAPProviderURL','ldap://192.168.111.15','config.ldap.provider.url','config.header.ldap','STRING',0),
('LDAPProvisioningEnabled','false','config.ldap.provisioning.enabled','config.header.ldap','BOOLEAN',1),
('LDAPRolesAttr','memberOf','admin.user.roles','config.header.ldap.attributes','STRING',0),
('LDAPSearchFilter','(cn={0})','config.ldap.search.filter','config.header.ldap','STRING',0),
('LDAPSearchResultsPageSize','100','config.ldap.search.results.page.size','config.header.ldap','LONG',0),
('LDAPSecurityAuthentication','simple','config.ldap.security.authentication','config.header.ldap','STRING',0),
('LDAPSecurityProtocol','','config.ldap.security.protocol','config.header.ldap','STRING',0),
('LDAPStateAttr','st','admin.user.state','config.header.ldap.attributes','STRING',0),
('LDAPUpdateOnLogin','true','config.ldap.update.on.login','config.header.ldap','BOOLEAN',1),
('LearnerScreenSize','1280x720','config.learner.screen.size','config.header.look.feel','STRING',1),
('LearningOutcomeQuickAddEnable','true','config.learning.outcome.add.enable','config.header.features','BOOLEAN',1),
('LockOutTime','5','config.lock.out.time','config.header.password.policy','LONG',1),
('LoginAsEnable','true','config.login.as.enable','config.header.privacy.settings','BOOLEAN',1),
('MonitorScreenSize','1280x720','config.monitor.screen.size','config.header.look.feel','STRING',1),
('PasswordExpirationMonths','12','config.password.expiration','config.header.password.policy','LONG',1),
('PasswordHistoryLimit','3','config.password.history','config.header.password.policy','LONG',1),
('PasswordPolicyLowercase','true','config.password.lowercase','config.header.password.policy','BOOLEAN',0),
('PasswordPolicyMinChars','8','config.password.minimum.characters','config.header.password.policy','LONG',1),
('PasswordPolicyNumerics','true','config.password.numerics','config.header.password.policy','BOOLEAN',0),
('PasswordPolicySymbols','false','config.password.symbols','config.header.password.policy','BOOLEAN',0),
('PasswordPolicyUppercase','true','config.password.uppercase','config.header.password.policy','BOOLEAN',0),
('ProfileEditEnable','true','config.profile.edit.enable','config.header.features','BOOLEAN',1),
('ProfilePartialEditEnable','true','config.profile.partial.edit.enable','config.header.features','BOOLEAN',1),
('QbCollectionsCreateEnable','true','config.qb.collections.create.enable','config.header.qb','BOOLEAN',1),
('QbCollectionsTransferEnable','true','config.qb.collections.transfer.enable','config.header.qb','BOOLEAN',1),
('QbMergeEnable','true','config.qb.merge.enable','config.header.qb','BOOLEAN',1),
('QbMonitorsReadOnly','false','config.qb.monitors.read.only','config.header.qb','BOOLEAN',1),
('QbQtiEnable','true','config.qb.qti.enable','config.header.qb','BOOLEAN',1),
('QbStatsGroupSize','27','config.qb.stats.group.size','config.header.qb','LONG',1),
('QbStatsMinParticipants','2','config.qb.stats.min.participants','config.header.qb','LONG',1),
('QbWordEnable','true','config.qb.word.enable','config.header.qb','BOOLEAN',1),
('RestrictedGroupUserNames','true','config.restricted.displaying.user.names.in.groupings','config.header.privacy.settings','BOOLEAN',0),
('ServerCountry','AU','config.server.country','config.header.look.feel','STRING',1),
('ServerLanguage','en_AU','config.server.language','config.header.look.feel','STRING',1),
('ServerPageDirection','LTR','config.server.page.direction','config.header.look.feel','STRING',1),
('ServerURL','http://localhost:8080/lams/','config.server.url','config.header.system','STRING',1),
('ServerURLContextPath','lams/','config.server.url.context.path','config.header.system','STRING',1),
('ServerVersionNumber','4.6','config.server.version.number','config.header.versions','STRING',1),
('ShowAllMyLessonLink','true','config.show.all.my.lesson.link','config.header.features','BOOLEAN',1),
('ShowTimezoneWarning','true','config.show.timezone.warning','config.header.features','BOOLEAN',1),
('SiteName','LAMS','config.site.name','config.header.system','STRING',1),
('SMTPAuthSecurity','none','config.smtp.auth.security','config.header.email','STRING',1),
('SMTPPassword','','config.smtp.password','config.header.email','STRING',0),
('SMTPPort','25','config.smtp.port','config.header.email','LONG',0),
('SMTPServer','','config.smtp.server','config.header.email','STRING',0),
('SMTPUser','','config.smtp.user','config.header.email','STRING',0),
('SuffixImportedLD','false','config.authoring.suffix','config.header.features','BOOLEAN',0),
('TempDir','@temp.directory@','config.temp.dir','config.header.system','STRING',1),
('UploadFileMaxMemorySize','4096','config.upload.file.max.memory.size','config.header.uploads','LONG',1),
('UploadFileMaxSize','10485760','config.upload.file.max.size','config.header.uploads','LONG',1),
('UploadLargeFileMaxSize','104857600','config.upload.large.file.max.size','config.header.uploads','LONG',1),
('UserInactiveTimeout','10800','config.user.inactive.timeout','config.header.system','LONG',1),
('UserValidationEmail','true','config.user.validation.emails','config.header.user.validation','BOOLEAN',0),
('UserValidationFirstLastName','true','config.user.validation.first.last.name','config.header.user.validation','BOOLEAN',0),
('UserValidationUsername','true','config.user.validation.username','config.header.user.validation','BOOLEAN',0),
('Version','4.6','config.version','config.header.system','STRING',1);