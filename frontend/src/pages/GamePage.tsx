import { useParams } from 'react-router';
import { useAppDispatch, useAppSelector } from '../app/hooks';
import { fetchGameById, selectGamesLoading, selectSelectedGame } from '../features/games/gameSlice';
import { useEffect } from 'react';

type Props = {}

export default function GamePage({ }: Props) {
  const { id } = useParams<{ id: string }>();
  const dispatch = useAppDispatch();
  const game = useAppSelector(selectSelectedGame);
  const loading = useAppSelector(selectGamesLoading)

  useEffect(() => {
    if (id) {
      const numericId = Number(id);
      dispatch(fetchGameById(numericId));
    }
  }, [dispatch])

  return (
    <>
      {
        loading ? (
          <p>Loading...</p>
        ) : (
          <>
            <div className='flex flex-col gap-4 pt-24 text-white/80 items-center'>
              <p className='text-4xl font-medium'>{game?.title}</p>
              <div className='pb-10'>
                <iframe src={`/builds/build-${game?.lastBuild?.id}/index.html`} width={1080} height={600}></iframe>
                <div className='flex border bg-secondary border-primary shadow-xl w-[1080px] h-32 px-4 py-2'>
                  <div className='flex flex-col justify-between gap-4 w-3/4 text-lg'>
                    <div>
                      <p>Category: {game?.category.name}</p>
                      <p>Tags: {game?.tags?.map((tag: any) => tag.name).join(", ") || "None"}</p>
                    </div>
                    <p>Published at: {new Date((game?.lastBuild?.updatedAt)!.toLocaleString()).toLocaleString()}</p>
                  </div>
                  <div className='w-1/4'>
                    <p className='text-xl'>Developed by: {game?.owner?.username} </p>
                  </div>
                </div>
              </div>

            </div>
          </>
        )
      }

    </>
  )
} 