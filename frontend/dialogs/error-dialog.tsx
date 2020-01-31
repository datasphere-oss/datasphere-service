import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <DkuModalHeaderWithTotem modal-title={title} modal-close=\"cancel\" modal-totem=\"icon-warning-sign\" />
    + 
    +             <div className=\"modal-body tight-body\">
    +                 <p dangerouslySetInnerHTML={{__html: 'text'}} />
    +             </div>
    +             <div
    +                 className=\"modal-footer modal-footer-std-buttons\"
    +                 global-keydown=\"{'enter':'confirm();', 'esc': 'confirm();' }\">
    +                 <button
    +                     className=\"btn btn-lg btn-danger\"
    +                     onClick={() => {
    +                         confirm();
    +                     }}>
    +                     好的
    +                 </button>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;