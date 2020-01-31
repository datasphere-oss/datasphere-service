import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             ng-controller=\"NotificationsReporterController\"
    +             project-integrations-editor=\"\"
    +             messaging=\"reporter.messaging\"
    +             messaging-variables-behavior=\"\">
    +             <div ng-include=\"'/templates/scenarios/fragments/reporter-header.html'\" />
    + 
    +             {!reporter.$hidden ? (
    +                 <form name=\"descriptionForm\" className=\"dkuform-horizontal reporter-form\">
    +                     {' '}
    +                     {/* ng-show is not sufficient, the codeMirror doesn't get refreshed */}
    +                     <div className=\"control-group\">
    +                         <div className=\"control-label\">类型</div>
    +                         <div className=\"controls\">
    +                             <select
    +                                 value={reporter.messaging.type}
    +                                 ng-options=\"type.id as type.label for type in integrationTypes\">
    +                                 消息通道 No available channel. You need to define them in the Administration section.
    +                                 Messaging channel No available channel. Your DSS administrator needs to define them in
    +                                 the Administration section. Messaging channel
    +                             </select>
    +                         </div>
    +                     </div>
    +                     {reporter.messaging.channelId ? (
    +                         <div>
    +                             {/* Then the messaging configuration itself */}
    +                             <div
    +                                 messaging-configuration-editor2=\"\"
    +                                 messaging=\"reporter.messaging\"
    +                                 reporter=\"reporter\"
    +                                 available-variables=\"availableVariables\"
    +                                 datasets=\"datasets\"
    +                                 dataset-smart-names=\"datasetSmartNames\">
    +                                 <div className=\"control-group\">
    +                                     <label htmlFor=\"\" className=\"control-label\">
    +                                         Raw format
    +                                     </label>
    +                                     <div className=\"controls\">
    +                                         <input type=\"checkbox\" value={reporter.rawFormat} />
    +                                         <span className=\"help-inline\">
    +                                             Send events as JSON rather than human readable strings
    +                                         </span>
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     ) : null}
    +                 </form>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;