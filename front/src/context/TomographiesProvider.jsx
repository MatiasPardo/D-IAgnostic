import { TomographiesContext } from './TomographiesContext'
import { UseTomographies } from '../hooks/UseTomographies'

export const TomographiesProvider = ({children}) => {

    const [tomographies, setTomographies] = UseTomographies();

    return (
        <TomographiesContext.Provider value={{ tomographies, getTomographies }}>
            {children}
        </TomographiesContext.Provider>
    );
}
