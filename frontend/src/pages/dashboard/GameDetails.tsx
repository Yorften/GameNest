import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../app/hooks';
import { fetchGameById, selectGamesError, selectGamesLoading, selectSelectedGame } from '../../features/games/gameSlice';
import NewGame from './NewGame';
import { useNavigate, useParams } from 'react-router';
import GameBuilds from '../../components/GameBuilds';

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
      className='[&>div]:!p-0 bg-[#1a1c1c] h-[94%]'
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
  const { id } = useParams<{ id: string }>();
  const [value, setValue] = useState(0);
  const navigate = useNavigate();

  const dispatch = useAppDispatch();
  const loadingGame = useAppSelector(selectGamesLoading);
  const selectedGame = useAppSelector(selectSelectedGame);
  const gameError = useAppSelector(selectGamesError);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  useEffect(() => {
    if (id) {
      const numericId = Number(id);
      dispatch(fetchGameById(numericId));
    }
  }, [dispatch])

  useEffect(() => {
    if (loadingGame === false && gameError?.status === 404) {
      navigate('/404', { replace: true });
    }
  }, [gameError, dispatch]);

  return (
    <>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
          <Tab className={`!text-white !bg-white/5`} label="Game Details" {...a11yProps(0)} />
          <Tab className={`!text-white !bg-white/5`} label="Builds" {...a11yProps(1)} />
        </Tabs>
      </Box>
      {loadingGame ? (
        <p>loading</p>
      ) : (
        <>
          <CustomTabPanel value={value} index={0}>
            <NewGame selectedGame={selectedGame} />
          </CustomTabPanel >
          <CustomTabPanel value={value} index={1}>
            <GameBuilds id={selectedGame?.id} />
          </CustomTabPanel></>
      )
      }

    </>
  )
}