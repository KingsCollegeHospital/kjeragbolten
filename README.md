"kjeragbolten"

Like the nordic boulder this is an MTA that sits between client smtp requests and the external e-mail server (send.nhs.net)

It redirects and convert MIME e-mails to CDA destinated for a GP surgery via the PCTI EDT hub -- e-mails not destinated for a GP surgery stay as an e-mail and continue on to the relay.

This project exists to allowing staging of migration from e-mail to the EDT hub for communication with the community.