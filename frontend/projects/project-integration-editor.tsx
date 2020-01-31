import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div>
    +             <form name=\"descriptionForm\" className=\"dkuform-horizontal reporter-header horizontal-flex\">
    +                 <span
    +                     className=\"flex cursor-pointer type-wrapper\"
    +                     onClick={() => {
    +                         integration.$expanded = !integration.$expanded;
    +                     }}>
    +                     <span className=\"expand-button\">
    +                         <i className=\"{'icon-chevron-down' : integration.$expanded, 'icon-chevron-right' : !integration.$expanded}\" />
    +                     </span>
    +                     {integration.hook.type ? <span className=\"type\">{integration.hook.type}</span> : null}
    +                 </span>
    +                 <span className=\"noflex active\">活动</span>
    +                 <label className=\"noflex dku-toggle\" style=\"margin-right: 30px;\">
    +                     <input type=\"checkbox\" value={integration.active} />
    +                     <span />
    +                 </label>
    +                 <a
    +                     className=\"noflex delete\"
    +                     aria-hidden=\"true\"
    +                     onClick={() => {
    +                         removeIntegration($index);
    +                     }}>
    +                     ×
    +                 </a>
    +             </form>
    + 
    +             {integration.$expanded && integration.hook.type ? (
    +                 <form name=\"descriptionForm\" className=\"dkuform-horizontal reporter-form\">
    +                     <div project-integration-params=\"\" hook=\"integration.hook\" />
    +                 </form>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;