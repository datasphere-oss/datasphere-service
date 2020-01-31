import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"h100 runnable-page\">
    +             <div block-api-error=\"\" />
    +             <div className=\"h100 oa horizontal-centerer\">
    +                 <div className=\"recipe-settings-section1 w800\">
    +                     <div runnable-run-button=\"\" runnable=\"runnable\" insight=\"insight\" run-output=\"runOutput\" />
    +                 </div>
    + 
    +                 <div className=\"recipe-settings-section1 w800\">
    +                     <form style=\"padding: 10px;\">
    +                         {runOutput.resultType && !runOutput.error && !runOutput.failure ? (
    +                             <div
    +                                 runnable-result=\"\"
    +                                 runnable=\"runnable\"
    +                                 result-label=\"runOutput.resultLabel\"
    +                                 result-type=\"runOutput.resultType\"
    +                                 result-data=\"runOutput.resultData\"
    +                             />
    +                         ) : null}
    +                         {runOutput.error ? (
    +                             <div api-error-alert=\"runOutput.error\">
    +                                 {runOutput.failure ? (
    +                                     <div api-error-alert=\"runOutput.failure\">
    +                                         {runOutput.logTail &&
    +                                         runOutput.logTail.lines &&
    +                                         (runOutput.failure || runOutput.error) ? (
    +                                             <pre smart-log-tail=\"runOutput.logTail\" />
    +                                         ) : null}
    +                                     </div>
    +                                 ) : null}
    +                             </div>
    +                         ) : null}
    +                     </form>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;