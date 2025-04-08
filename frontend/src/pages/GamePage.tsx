import { Link, useParams } from 'react-router';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import { fetchGameById, selectGamesLoading, selectSelectedGame } from '../features/games/gameSlice';
import { useEffect } from 'react';
import useMediaQuery from '../hooks/useMediaQuery';
import { FaChevronCircleLeft, FaRegStar } from "react-icons/fa";


type Props = {}

export default function GamePage({ }: Props) {
  const { id } = useParams<{ id: string }>();
  const dispatch = useAppDispatch();
  const game = useAppSelector(selectSelectedGame);
  const loading = useAppSelector(selectGamesLoading);

  const isLargeScreen = useMediaQuery('(min-width: 1024px)');

  const gameUrl = import.meta.env.VITE_GAME_BUILDS_URL;

  useEffect(() => {
    if (id && isLargeScreen) {
      const numericId = Number(id);

      dispatch(fetchGameById(numericId));
    }
  }, [dispatch, isLargeScreen])

  return (
    <>
      {isLargeScreen ? (
        <>
          {loading ? (
            <div className='flex justify-center items-center pt-24 text-white/80'>
              <p>Loading Game...</p>
            </div>
          ) : game ? (

            <div className='flex flex-col gap-4 pt-24 text-white/80 items-center'>
              <div className='flex items-center justify-between w-[1080px]'>
                <Link to="/games" className='flex items-center gap-2 hover:text-white'>
                  <FaChevronCircleLeft className='h-6 w-6' />
                  <p>Back</p>
                </Link>
                <p className='text-4xl font-medium'>{game.title}</p>
                <FaRegStar className='h-6 w-6 hover:fill-yellow-400 cursor-pointer' />
              </div>
              <div className='pb-10'>
                {game.lastBuild?.id && (
                  <iframe
                    src={`${gameUrl}/build-${game.lastBuild.id}/index.html`}
                    width={1080}
                    height={600}
                    style={{ border: '1px solid black' }}
                    title={`${game.title} Build`}
                    allow="cross-origin-isolated"
                  />
                )}
                <div className='flex border bg-secondary border-primary shadow-xl w-[1080px] h-32 px-4 py-2'>
                  <div className='flex flex-col justify-between gap-4 w-3/4 text-lg'>
                    <div>
                      <p>Category: {game.category?.name ?? 'N/A'}</p>
                      <p>Tags: {game.tags?.map((tag: any) => tag.name).join(", ") || "None"}</p>
                    </div>
                    <p>Published at: {game.lastBuild?.updatedAt ? new Date(game.lastBuild.updatedAt).toLocaleString() : 'N/A'}</p>
                  </div>
                  <div className='w-1/4'>
                    <p className='text-xl'>Developed by: {game.owner?.username ?? 'Unknown'}</p>
                  </div>
                </div>
              </div>
            </div>
          ) : (
            <div className='flex justify-center items-center pt-24 text-white/80'>
              <p>Could not load game data.</p>
            </div>
          )}
        </>
      ) : (
        <div className='flex flex-col items-center justify-center h-screen gap-4 pt-24 text-white/80 px-4'> {/* Removed 'lg:hidden flex', added padding */}
          <div className='pb-10 text-center'>
            <img className='w-full max-w-xs h-auto object-cover mx-auto mb-4' src="/assets/responsivity.svg" alt="Illustration showing a large screen" /> {/* Ensure correct path */}
            <p className='text-xl'>Please switch to a larger screen (desktop) for the best experience!</p>
          </div>
        </div>
      )}
    </>
  );

} 