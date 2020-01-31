import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 ee-unavailable-feature-modal\" auto-size=\"false\">
    +             <div dku-modal-header=\"\" modal-title=\"Feature unavailable\">
    +                 <div className=\"modal-body\">
    +                     <div className=\"locked-feature\" style=\"text-align: center; white-space: pre-line\">
    +                         {lockedMessage}
    +                     </div>
    +                 </div>
    +                 <div className=\"modal-footer modal-footer-std-buttons\">
    +                     <a type=\"submit\" className=\"btn btn-primary\" target=\"_blank\" href={learnMoreURL}>
    +                         了解更多
    +                     </a>
    +                     <button type=\"button\" className=\"btn btn-default\" data-dismiss=\"modal\">
    +                         关闭
    +                     </button>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;