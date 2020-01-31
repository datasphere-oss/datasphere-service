import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <div dku-modal-header=\"\" modal-title=\"Contact your administrator\">
    +                 <div className=\"modal-body\" style=\"padding-bottom: 0\">
    +                     <div from-markdown=\"$root.appConfig.studioAdminContact\" />
    +                 </div>
    +                 <div
    +                     className=\"modal-footer modal-footer-std-buttons\"
    +                     global-keydown=\"{'enter':'dismiss();', 'esc': 'dismiss();' }\">
    +                     <button
    +                         className=\"btn btn-lg btn-primary\"
    +                         onClick={() => {
    +                             dismiss();
    +                         }}>
    +                         OK
    +                     </button>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;