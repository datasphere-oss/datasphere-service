import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <span className=\"abort\">
    +             {!aborting ? (
    +                 <button
    +                     type=\"button\"
    +                     className=\"close\"
    +                     onClick={() => {
    +                         showConfirmationForm();
    +                     }}
    +                     aria-hidden=\"true\">
    +                     ×
    +                 </button>
    +             ) : null}
    +             {aborting ? (
    +                 <span className=\"confirmation\">
    +                     <span className=\"form-wrapper\">
    +                         <button
    +                             type=\"button\"
    +                             className=\"btn btn-danger\"
    +                             onClick={() => {
    +                                 abort();
    +                                 hideConfirmationForm();
    +                             }}>
    +                             Confirm
    +                         </button>
    +                         <button
    +                             type=\"button\"
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 hideConfirmationForm();
    +                             }}>
    +                             Cancel
    +                         </button>
    +                     </span>
    +                     <span className=\"overlay\" />
    +                 </span>
    +             ) : null}
    +         </span>
    +     );
    + };
    + 
    + export default TestComponent;