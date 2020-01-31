import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className={`nbk-cell ${rowIndex % 2 ? 'odd' : 'even'}-row`} title={cell}>
    +             {cell}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;