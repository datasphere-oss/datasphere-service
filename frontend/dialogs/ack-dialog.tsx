import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <DkuModalHeader modal-title={title} modal-close=\"cancel\" />
    +             <div className=\"modal-body\">
    +                 <p dangerouslySetInnerHTML={{__html: 'text'}} />
    +             </div>
    +             <div
    +                 className=\"modal-footer modal-footer-std-buttons\"
    +                 global-keydown=\"{'enter':'confirm();', 'esc': 'confirm();' }\">
    +                 <button
    +                     className=\"btn btn-lg btn-success\"
    +                     onClick={() => {
    +                         confirm();
    +                     }}>
    +                     OK
    +                 </button>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;