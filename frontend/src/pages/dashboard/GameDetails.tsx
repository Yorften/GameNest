import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import { useState } from 'react';
import { useAppSelector } from '../../app/hooks';
import { selectGamesLoading, selectSelectedGame } from '../../features/games/gameSlice';
import NewGame from './NewGame';

type Props = {}

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function CustomTabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  );
}

function a11yProps(index: number) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function GameDetails({ }: Props) {
  const [value, setValue] = useState(0);
  const loadingGame = useAppSelector(selectGamesLoading);
  const selectedGame = useAppSelector(selectSelectedGame);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
          <Tab className='!text-white !bg-white/5' label="Game Details" {...a11yProps(0)} />
          <Tab className='!text-white !bg-white/5' label="Builds" {...a11yProps(1)} />
        </Tabs>
      </Box>
      <CustomTabPanel value={value} index={0}>
        <NewGame selectedGame={selectedGame} />
      </CustomTabPanel>
      <CustomTabPanel value={value} index={1}>
        Item Two
      </CustomTabPanel>
    </>
  )
}