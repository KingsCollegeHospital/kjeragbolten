package uk.kch.nhs.geneva.kjeragbolten;

public class CcdDocumentData {
	
	public CcdDocumentData() {
		this.setAuthorSig("");
		this.setRepresentedCustodianOrganizationId("2.16.840.1.113883.19.5");
		this.setRoot("2.16.840.1.113883.19.4");
		this.setExtension("266");
		this.setProviderOrganisationId("2.16.840.1.113883.19.5");
		this.setRepresentedOrgainisationId("2.16.840.1.113883.19.5");
		this.setAssignedAuthorRoot("2.16.840.1.113883.19.5");
		this.setVersionValue("1");
		this.setPatientRoleIdRoot("2.16.840.1.113883.2.1.3.2.4.18.23");
		this.setAdministrativeGenderCode("2.16.840.1.113883.5.1");
		this.setInfrastructureRootId("2.16.840.1.113883.19.5");
	}

	private String PatientRoleId;
	private String Title;
	private String PatientFamily;
	private String PatientGender;
	private String PatientBirthdate;
	private String PatientGiven;
	private String AuthorGiven;
	private String AuthorFamily;
	private String EffectiveTimeValue;
	private String RecipientFamily;
	private String RecipientGiven;
	private String ReceivedOrgainisationName;
	private String OrganisationName;
	
	private String AuthorSig;
	private String RepresentedCustodianOrganizationId;
	private String Root;
	private String Extension;
	private String ProviderOrganisationId;
	private String RepresentedOrgainisationId;
	private String AssignedAuthorRoot;
	private String VersionValue;
	private String PatientRoleIdRoot;
	private String AdministrativeGenderCode;
	private String InfrastructureRootId;
	
	private String Destination;
	
	public String getTitle() {
		return Title;
	}
	
	public void setTitle(String title) {
		Title = title;
	}
	
	public String getPatientFamily() {
		return PatientFamily;
	}
	
	public void setPatientFamily(String patientFamily) {
		PatientFamily = patientFamily;
	}
	
	public String getPatientGender() {
		return PatientGender;
	}
	
	public void setPatientGender(String patientGender) {
		PatientGender = patientGender;
	}
	
	public String getPatientBirthdate() {
		return PatientBirthdate;
	}
	
	public void setPatientBirthdate(String patientBirthdate) {
		PatientBirthdate = patientBirthdate;
	}
	
	public String getPatientGiven() {
		return PatientGiven;
	}
	
	public void setPatientGiven(String patientGiven) {
		PatientGiven = patientGiven;
	}
	
	public String getAuthorGiven() {
		return AuthorGiven;
	}
	
	public void setAuthorGiven(String authorGiven) {
		AuthorGiven = authorGiven;
	}
	
	public String getAuthorFamily() {
		return AuthorFamily;
	}
	
	public void setAuthorFamily(String authorFamily) {
		AuthorFamily = authorFamily;
	}
	
	public String getEffectiveTimeValue() {
		return EffectiveTimeValue;
	}
	
	public void setEffectiveTimeValue(String effectiveTimeValue) {
		EffectiveTimeValue = effectiveTimeValue;
	}
	
	public String getRecipientFamily() {
		return RecipientFamily;
	}
	
	public void setRecipientFamily(String recipientFamily) {
		RecipientFamily = recipientFamily;
	}
	
	public String getRecipientGiven() {
		return RecipientGiven;
	}
	
	public void setRecipientGiven(String recipientGiven) {
		RecipientGiven = recipientGiven;
	}
	
	public String getReceivedOrgainisationName() {
		return ReceivedOrgainisationName;
	}
	
	public void setReceivedOrgainisationName(String receivedOrgainisationName) {
		ReceivedOrgainisationName = receivedOrgainisationName;
	}
	
	public String getOrganisationName() {
		return OrganisationName;
	}
	
	public void setOrganisationName(String organisationName) {
		OrganisationName = organisationName;
	}
	
	public String getAuthorSig() {
		return AuthorSig;
	}
	
	public void setAuthorSig(String authorSig) {
		AuthorSig = authorSig;
	}
	
	public String getRepresentedCustodianOrganizationId() {
		return RepresentedCustodianOrganizationId;
	}
	
	public void setRepresentedCustodianOrganizationId(
			String representedCustodianOrganizationId) {
		RepresentedCustodianOrganizationId = representedCustodianOrganizationId;
	}
	
	public String getRoot() {
		return Root;
	}
	
	public void setRoot(String root) {
		Root = root;
	}
	
	public String getExtension() {
		return Extension;
	}
	
	public void setExtension(String extension) {
		Extension = extension;
	}
	
	public String getProviderOrganisationId() {
		return ProviderOrganisationId;
	}
	
	public void setProviderOrganisationId(String providerOrganisationId) {
		ProviderOrganisationId = providerOrganisationId;
	}
	
	public String getRepresentedOrgainisationId() {
		return RepresentedOrgainisationId;
	}
	
	public void setRepresentedOrgainisationId(String representedOrgainisationId) {
		RepresentedOrgainisationId = representedOrgainisationId;
	}
	
	public String getAssignedAuthorRoot() {
		return AssignedAuthorRoot;
	}
	
	public void setAssignedAuthorRoot(String assignedAuthorRoot) {
		AssignedAuthorRoot = assignedAuthorRoot;
	}
	
	public String getVersionValue() {
		return VersionValue;
	}
	
	public void setVersionValue(String versionValue) {
		VersionValue = versionValue;
	}
	
	public String getPatientRoleIdRoot() {
		return PatientRoleIdRoot;
	}
	
	public void setPatientRoleIdRoot(String patientRoleIdRoot) {
		PatientRoleIdRoot = patientRoleIdRoot;
	}
	
	public String getAdministrativeGenderCode() {
		return AdministrativeGenderCode;
	}
	
	public void setAdministrativeGenderCode(String administrativeGenderCode) {
		AdministrativeGenderCode = administrativeGenderCode;
	}
	
	public String getInfrastructureRootId() {
		return InfrastructureRootId;
	}
	
	public void setInfrastructureRootId(String infrastructureRootId) {
		InfrastructureRootId = infrastructureRootId;
	}

	public String getPatientRoleId() {
		return PatientRoleId;
	}
	
	public void setPatientRoleId(String patientRoleId) {
		PatientRoleId = patientRoleId;
	}

	public String getDestination() {
		return Destination;
	}

	public void setDestination(String destination) {
		Destination = destination;
	}

}
