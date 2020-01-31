import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <div key=\"child-0\" block-api-error=\"\" />,
    + 
    +         $root.appConfig.webappsIsolationMode == 'SANDBOX' ? (
    +             <div key=\"child-2\" className=\"h100\">
    +                 <iframe
    +                     id=\"qa_insights_webapp-view\"
    +                     className=\"webapp-container\"
    +                     sandbox=\"allow-forms allow-pointer-lock allow-popups allow-scripts\"
    +                     src={iFrameUrl}
    +                     frameBorder={0}
    +                     width=\"100%\"
    +                     height=\"100%\">{`
    +     `}</iframe>
    +             </div>
    +         ) : null,
    +         $root.appConfig.webappsIsolationMode != 'SANDBOX' ? (
    +             <div key=\"child-4\" className=\"h100\">
    +                 <iframe
    +                     id=\"qa_insights_webapp-view\"
    +                     className=\"webapp-container\"
    +                     src={iFrameUrl}
    +                     frameBorder={0}
    +                     width=\"100%\"
    +                     height=\"100%\">{`
    +     `}</iframe>
    +             </div>
    +         ) : null
    +     ];
    + };
    + 
    + export default TestComponent;