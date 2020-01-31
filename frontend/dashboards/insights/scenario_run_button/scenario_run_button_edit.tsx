import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return <div scenario-insight-create-form=\"\" insight=\"insight\" />;
    + };
    + 
    + export default TestComponent;