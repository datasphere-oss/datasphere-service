import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"banner check oa\">
    +             <table style=\"width: 100%; table-layout: auto;\">
    +                 <tbody>
    +                     <tr className=\"metric-plot\">
    +                         <td className=\"info\" style={`left: ${adjustForScroll.left}px; z-index: 10; width: auto`}>
    +                             <div className=\"tile-wrapper\">
    +                                 <h4 className=\"tile-header\" title={displayedCheck.name}>
    +                                     {displayedCheck.name}
    +                                 </h4>
    +                                 <div className=\"tile-body\" style=\"overflow: hidden;\">
    +                                     <div className=\"date\">{date(lastValue.time, 'yyyy-MM-dd HH:mm')}</div>
    +                                     <div className=\"message\">{lastValue.message}</div>
    +                                     <div className={`single-outcome outcome-${lastValue.outcome.toLowerCase()}`}>
    +                                         <div className=\"value\">{lastValue.outcome}</div>
    +                                     </div>
    +                                     <div className={`outcome-bar outcome-${lastValue.outcome.toLowerCase()}`} />
    +                                 </div>
    +                             </div>
    +                         </td>
    + 
    +                         {displayedData ? (
    +                             <td className=\"card\" style=\"width: 100%;\">
    +                                 <div className=\"horizontal-list\">
    +                                     {displayedData.values.filter(pointInRange).map((point, index: number) => {
    +                                         return (
    +                                             <div key={`item-${index}`} className=\"point\">
    +                                                 <h4 className=\"tile-header\" />
    +                                                 <div className=\"tile-body\">
    +                                                     <div className=\"date\">{date(point.time, 'yyyy-MM-dd HH:mm')}</div>
    +                                                     <div className=\"message\">{point.message}</div>
    +                                                     <div
    +                                                         className={`single-outcome outcome-${point.outcome.toLowerCase()}`}>
    +                                                         <div className=\"value\">{point.outcome}</div>
    +                                                     </div>
    +                                                     <div
    +                                                         className={`outcome-bar outcome-${point.outcome.toLowerCase()}`}
    +                                                     />
    +                                                 </div>
    +                                             </div>
    +                                         );
    +                                     })}
    +                                 </div>
    +                             </td>
    +                         ) : null}
    +                     </tr>
    +                 </tbody>
    +             </table>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;