import React from 'react';

const HubspotContactForm = props => {
    const { region, portalId, formId } = props;
    const formCode = `
    <script charset="utf-8" type="text/javascript" src="//js.hsforms.net/forms/shell.js"></script>
    <script>
      hbspt.forms.create({
        region: "${region}",
        portalId: "${portalId}",
        formId: "${formId}"
    });
    </script>
    `;
    return (
        <div id="hubspotForm" dangerouslySetInnerHTML={{__html: formCode}} />
  );
};

export default HubspotContactForm;