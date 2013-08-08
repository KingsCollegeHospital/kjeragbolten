package uk.kch.nhs.geneva.kjeragbolten;

class CcdDocumentData(
    var PatientRoleId : String,
    var Title : String,
    var PatientFamily : String,
    var PatientGender : String,
    var PatientBirthdate : String,
    var PatientGiven : String,
    var AuthorGiven : String,
    var AuthorFamily : String,
    var EffectiveTimeValue : String,
    var RecipientFamily : String,
    var RecipientGiven : String,
    var ReceivedOrgainisationName : String,
    var OrganisationName : String,
    var AuthorSig : String,
    var RepresentedCustodianOrganizationId : String,
    var Root : String,
    var Extension : String,
    var ProviderOrganisationId : String,
    var RepresentedOrgainisationId : String,
    var AssignedAuthorRoot : String,
    var Versionvarue : String,
    var PatientRoleIdRoot : String,
    var AdministrativeGenderCode : String,
    var InfrastructureRootId : String,
    var Destination : String) {

  def this() = {
    this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "",
        "2.16.840.1.113883.19.5",
        "2.16.840.1.113883.19.4",
        "266", "2.16.840.1.113883.19.5",
        "2.16.840.1.113883.19.5",
        "2.16.840.1.113883.19.5",
        "1",
        "2.16.840.1.113883.2.1.3.2.4.18.23",
        "2.16.840.1.113883.5.1",
        "2.16.840.1.113883.19.5",
        null);
  }

}
